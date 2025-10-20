import PIL.Image
import numpy as np
import photo_processor

img = np.asarray(PIL.Image.open('test.jpg'))
img_np = img  


processor = photo_processor.ImageProcessor()

result = processor.process_photo(img, year="1950", location="Lviv")
PIL.Image.fromarray(result).save("processed.jpg")



def process_image(img_url):
    img = np.asarray(PIL.Image.open(input_path))
    processor = photo_processor.ImageProcessor()
    result = processor.process_photo(img, year=year, location=location)
    PIL.Image.fromarray(result).save(output_path)