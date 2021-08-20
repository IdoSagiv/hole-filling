import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;

public class Main {
    // Compulsory
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String CORRECT_USAGE_MSG = "Usage: {image_path} {hole_image_path} {out_path} {z} {epsilon} {connectivity_type}\n" +
            "where: z and epsilon are floats and connectivity_type = 4 or 8";

    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("Invalid usage.\n" + CORRECT_USAGE_MSG);
            return;
        }

        Mat img = Imgcodecs.imread(args[0], Imgcodecs.IMREAD_GRAYSCALE);
        Mat mask = Imgcodecs.imread(args[1], Imgcodecs.IMREAD_GRAYSCALE);

        if (img.empty() || mask.empty()) {
            System.out.println("Invalid input images\n" + CORRECT_USAGE_MSG);
            return;
        }

        double z, eps;
        int connectivity;
        try {
            z = Double.parseDouble(args[3]);
            eps = Double.parseDouble(args[4]);
            connectivity = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid z, epsilon values\n" + CORRECT_USAGE_MSG);
            return;
        }

        NeighborsFunction N;
        switch (connectivity) {
            case 4: {
                N = new FourNeighbors();
                break;
            }
            case 8: {
                N = new EightNeighbors();
                break;
            }
            default: {
                System.out.println("Invalid connectivity type\n" + CORRECT_USAGE_MSG);
                return;
            }
        }

        WeightFunction W = new LightricksWeightFunction(z, eps);

        fillHole(img, mask, args[2], W, N);
    }

    /**
     * @param imgOrig original image, grayscale values in range of [0,255]
     * @param mask    mask image, , grayscale values in range of [0,255]
     * @param outPath path of the result image
     * @param W       weight function
     * @param N       neighbors function
     */
    private static void fillHole(Mat imgOrig, Mat mask, String outPath, WeightFunction W, NeighborsFunction N) {
        HoleFiller filler = new HoleFiller(W, N);

        // create the corrupted image with the hole
        Mat corruptedImg = applyHoleMask(imgOrig, mask);
        //  todo: delete - for debug
//        System.out.println("**************** fixed image");
//        printMat(corruptedImg);

        //  fill the hole
        Mat fixedImage = filler.fillHole(corruptedImg);
        //  todo: delete - for debug
//        System.out.println("\n\n**************** fixed image");
//        printMat(fixedImage);

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
                System.out.format("%2f , ", m.get(i, j)[0]);
            }
            System.out.println();
        }
    }

}