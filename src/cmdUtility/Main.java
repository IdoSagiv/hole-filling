package cmdUtility;

import HoleFiller.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.html.HTMLCollection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;

public class Main {
    // Compulsory
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String CORRECT_USAGE_MSG = "Usage: {image_path} {hole_image_path} {z} {epsilon} {connectivity_type}\n" +
            "where: z and epsilon are floats and connectivity_type = 4 or 8";

    public static void main(String[] args) {
        try {
            Mat img = Imgcodecs.imread(args[0], Imgcodecs.IMREAD_GRAYSCALE);
            Mat mask = Imgcodecs.imread(args[1], Imgcodecs.IMREAD_GRAYSCALE);
            double z = Double.parseDouble(args[2]);
            double eps = Double.parseDouble(args[3]);
            int connectivity = Integer.parseInt(args[4]);


            if (img.empty() || mask.empty()) {
                throw new InvalidImageException();
            }

            NeighborsFunction N;
            if (connectivity == 4) {
                N = new FourNeighbors();
            } else if (connectivity == 8) {
                N = new EightNeighbors();
            } else {
                throw new InvalidConnectivityTypeException();
            }

            WeightFunction W = (u, v) -> {
                double subNorm = Math.sqrt(Math.pow(u.x - v.x, 2) + Math.pow(u.y - v.y, 2));
                return 1 / (Math.pow(subNorm, z) + eps);
            };

            Pattern p = Pattern.compile("(.*)\\.(.*)");
            Matcher m = p.matcher(args[0]);
            m.find();
            String outPath = m.group(1) + "_fixed." + m.group(2);

            fillHole(img, mask, outPath, W, N);


        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid usage\n" + CORRECT_USAGE_MSG);
            System.exit(1);
        } catch (InvalidImageException e) {
            System.out.println("Invalid input images\n" + CORRECT_USAGE_MSG);
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid z, epsilon values\n" + CORRECT_USAGE_MSG);
            System.exit(1);
        } catch (InvalidConnectivityTypeException e) {
            System.out.println("Invalid connectivity type\n" + CORRECT_USAGE_MSG);
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * @param imgOrig original image, grayscale values in range of [0,255]
     * @param mask    mask image, , grayscale values in range of [0,255]
     * @param outPath path of the result image
     * @param W       weight function
     * @param N       neighbors function
     */
    private static void fillHole(Mat imgOrig, Mat mask, String outPath, WeightFunction W, NeighborsFunction N) {
        // create the corrupted image with the hole
        Mat corruptedImg = applyHoleMask(imgOrig, mask);

        //  fill the hole
        Mat fixedImage = HoleFiller.fillHole(corruptedImg, W, N);

        //Write the image
        fixedImage.convertTo(fixedImage, CvType.CV_32FC1, 255f); // values to [0-1] range

        Imgcodecs.imwrite(outPath, fixedImage);
    }

    /**
     * @param img  grayscale image with values in range [0,255]
     * @param mask grayscale image with values in range [0,255], values lower than 127 considered as hole
     * @return new image with values in range [0,1] where the holes pixels are with value -1
     */
    private static Mat applyHoleMask(Mat img, Mat mask) {
        img.convertTo(img, CvType.CV_32FC1, 1.f / 255); // values to [0-1] range
        Imgproc.threshold(mask, mask, 127, 255, THRESH_BINARY_INV); // set the mask to binary 0/255
        return img.setTo(new Scalar(-1), mask);
    }

    //  todo: delete - for debug
    private static void printMat(Mat m) {
        for (int i = 0; i < m.height(); i++) {
            for (int j = 0; j < m.width(); j++) {
                System.out.format("%.2f , ", m.get(i, j)[0]);
            }
            System.out.println();
        }
    }
}