from setuptools import setup, Extension
import pybind11

ext_modules = [
    Extension(
        "photo_processor",
        ["../cpp/src/processor.cpp",  "../cpp/src/image_processor.cpp"],
        include_dirs=[
          pybind11.get_include(),
          "../cpp/include",   # Додайте шлях до ваших заголовків
          "/usr/include/opencv4"  # вже є
        ],
        libraries=["opencv_core", "opencv_imgproc", "opencv_objdetect", "opencv_imgcodecs"],
        library_dirs=["/usr/lib/x86_64-linux-gnu"],
        language="c++",
    )
]

setup(
    name="photo_processor",
    version="0.1",
    ext_modules=ext_modules,
)
