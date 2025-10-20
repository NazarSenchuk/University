#include <pybind11/pybind11.h>
#include <pybind11/numpy.h>
#include <pybind11/stl.h>

#include <opencv2/opencv.hpp>
#include "image_processor.hpp"

namespace py = pybind11;

// Функція конвертації NumPy→cv::Mat
cv::Mat numpy_uint8_to_mat(py::array_t<uint8_t>& input) {
    auto buf = input.request();
    if (buf.ndim != 3) throw std::runtime_error("Expecting 3 dimensions (H, W, C)");
    int height = buf.shape[0];
    int width  = buf.shape[1];
    int channels = buf.shape[2];
    // припускаємо CV_8UC3
    cv::Mat mat(height, width, CV_8UC3, (uint8_t*)buf.ptr, buf.strides[0]);
    // Якщо stride не стандартний, може бути необхідно копіювати
    return mat.clone();  // клон, щоб мати власний буфер, або можна спробувати без копії
}

// Функція конвертації cv::Mat→NumPy
py::array_t<uint8_t> mat_to_numpy_uint8(cv::Mat& mat) {
    // припускаємо CV_8UC3
    std::vector<ssize_t> shape = { mat.rows, mat.cols, mat.channels() };
    std::vector<ssize_t> strides = {
        mat.step[0],
        mat.step[1],
        sizeof(uint8_t)
    };
    return py::array_t<uint8_t>(shape, strides, mat.data);
}

PYBIND11_MODULE(photo_processor, m) {
    py::class_<ImageProcessor>(m, "ImageProcessor")
        .def(py::init<>())  // без аргументів
        .def("process_photo",
            [](ImageProcessor& self,
                py::array_t<uint8_t>& img,
                const std::string& year,
                const std::string& location) {
                cv::Mat mat = numpy_uint8_to_mat(img);
                cv::Mat result = self.process_photo(mat, year, location);
                return mat_to_numpy_uint8(result);
            },
            py::arg("img"),
            py::arg("year"),
            py::arg("location")
    );

    m.def("hello", []() { return "Hello from C++!"; });
    m.def("get_version", []() { return "1.0.0"; });

}
