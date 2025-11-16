
#include "image_processor.hpp"
#include <opencv2/imgproc.hpp>
#include <opencv2/imgcodecs.hpp>
#include <filesystem>
#include <stdexcept>
#include <vector>
ImageProcessor::ImageProcessor() {
    std::string face_cascade_path = "./haarcascade_frontalface_default.xml";
    if (!face_cascade_.load(face_cascade_path)) {
        throw std::runtime_error("Failed to load face cascade from " + face_cascade_path);
    }
}
cv::Mat ImageProcessor::process_photo(cv::Mat image, const std::string& year, const std::string& location) {
    if (image.empty()) {
        throw std::runtime_error("Empty image passed to process_photo");
    }

    detect_and_draw_faces(image);

    draw_text_info(image, year, location);

    return image;
}

std::vector<cv::Rect>  ImageProcessor::detect_and_draw_faces(cv::Mat& image) {
    cv::Mat gray;
    cv::cvtColor(image, gray, cv::COLOR_BGR2GRAY);
    cv::equalizeHist(gray, gray);

    std::vector<cv::Rect> faces;
    double scaleFactor = 1.1;
    int minNeighbors = 3;
    cv::Size minSize(30, 30);

    // Detect faces
    face_cascade_.detectMultiScale(gray, faces, scaleFactor, minNeighbors, 0, minSize);

    // Draw rectangles around detected faces
    for (const auto& face : faces) {
        cv::rectangle(image, face, cv::Scalar(0, 255, 0), 2);
    }

    return faces;
}



void ImageProcessor::draw_text_info(cv::Mat& image, const std::string& year, const std::string& location) {
    int font = cv::FONT_HERSHEY_COMPLEX;
    double scale = std::min(image.cols, image.rows) / 1000.0;
    scale = std::max(scale, 0.6);
    int thickness = std::max(static_cast<int>(round(scale * 2)), 1);

    std::string text1 = "Year: " + year;
    std::string text2 = "Location: " + location;

    int baseline = 0;
    cv::Size size1 = cv::getTextSize(text1, font, scale, thickness, &baseline);
    cv::Size size2 = cv::getTextSize(text2, font, scale, thickness, &baseline);

    int margin = 10;
    cv::Point org1(margin, margin + size1.height);
    cv::Point org2(margin, margin + size1.height + margin + size2.height);

    // Draw background rectangle with transparency
    int boxWidth = std::max(size1.width, size2.width) + margin * 2;
    int boxHeight = size1.height + size2.height + margin * 3;
    cv::Rect boxRect(margin / 2, margin / 2, boxWidth, boxHeight);
    cv::Mat roi = image(boxRect);
    cv::Mat overlay;
    roi.copyTo(overlay);
    overlay.setTo(cv::Scalar(0, 0, 0));
    cv::addWeighted(overlay, 0.5, roi, 0.5, 0, roi);

    // Draw texts
    cv::putText(image, text1, org1, font, scale, cv::Scalar(255, 255, 255), thickness, cv::LINE_AA);
    cv::putText(image, text2, org2, font, scale, cv::Scalar(255, 255, 255), thickness, cv::LINE_AA);
}
