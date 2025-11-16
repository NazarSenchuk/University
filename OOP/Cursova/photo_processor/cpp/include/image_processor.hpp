#ifndef IMAGE_PROCESSOR_HPP
#define IMAGE_PROCESSOR_HPP

#include <string>
#include <vector>
#include <opencv2/opencv.hpp>

class ImageProcessor {
public:
    ImageProcessor();  // без параметрів
    cv::Mat process_photo(cv::Mat image,
                          const std::string& year,
                          const std::string& location);

private:
    cv::CascadeClassifier face_cascade_;
    std::string output_dir_ = "location_processed";  // дефолт

    std::vector<cv::Rect> detect_and_draw_faces(cv::Mat& image);
    void draw_text_info(cv::Mat& image, const std::string& year, const std::string& location);

};

#endif // IMAGE_PROCESSOR_HPP
