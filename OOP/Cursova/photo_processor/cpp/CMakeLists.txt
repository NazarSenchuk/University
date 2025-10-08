cmake_minimum_required(VERSION 3.4)
project(photo_processor)


find_package(pybind11 REQUIRED)
find_package(OpenCV REQUIRED)
add_executable(opencv_demo src/opencv.cpp)
target_link_libraries(opencv_demo ${OpenCV_LIBS})

pybind11_add_module(photo_processor src/processor.cpp)  