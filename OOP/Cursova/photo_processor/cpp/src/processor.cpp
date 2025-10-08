// cpp/src/processor.cpp
#include <pybind11/pybind11.h>
namespace py = pybind11;

PYBIND11_MODULE(photo_processor, m) {
    m.def("hello", []() {
        return "Hello from C++!";
    });
    
    m.def("get_version", []() {
        return "1.0.0";
    });
}