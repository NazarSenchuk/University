#include <opencv2/opencv.hpp>
#include <iostream>

using namespace cv;
using namespace std;

int main() {
    Mat image = imread("image.jpg");
    
    if (image.empty()) {
        cout << "Could not open or find the image" << endl;
        return -1;
    }
    
    namedWindow("Display Window", WINDOW_AUTOSIZE);
    imshow("Display Window", image);
    
    waitKey(0);
    return 0;
}