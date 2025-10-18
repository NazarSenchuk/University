from setuptools import setup, Extension
import pybind11

ext_modules = [
    Extension(
        "photo_processor", 
        ["../cpp/src/processor.cpp"],  
        include_dirs=[pybind11.get_include()], 
        language="c++", 
    )
]

setup(
    name="photo_processor",
    version="0.1",
    ext_modules=ext_modules,
)