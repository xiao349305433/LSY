cmake_minimum_required(VERSION 3.4.1)

#include(${CMAKE_SOURCE_DIR}/include)

aux_source_directory(. SOURCE)

add_library(
        YuvUtil
        SHARED
        ${SOURCE}
)

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

target_link_libraries(
        YuvUtil
        ${log-lib}
)
