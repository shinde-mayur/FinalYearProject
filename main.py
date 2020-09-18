from flask import Flask, request
# from keras.preprocessing import image
import base64
import werkzeug
import os
from datetime import datetime
from keras.preprocessing import image
import tensorflow.compat.v1 as tf
import numpy as np
from keras.models import load_model

app = Flask(__name__)

tf.disable_v2_behavior()

graph = tf.get_default_graph()
print("------------------")
classes = ['Apple scab',
           'Apple Black rot',
           'Apple Cedar rust',
           'Apple healthy',
           'Blueberry healthy',
           'Cherry Powdery mildew',
           'Cherry healthy',
           'Corn Cercospora leaf spot Gray leaf spot',
           'Corn Common rust ',
           'Corn Northern Leaf Blight',
           'Corn healthy',
           'Grape Black rot',
           'Grape Esca',
           'Grape Leaf blight',
           'Grape healthy',
           'Orange Citrus greening',
           'Peach Bacterial spot',
           'Peach healthy',
           'Pepper bell Bacterial spot',
           'Pepper bell healthy',
           'Potato Early blight',
           'Potato Late blight',
           'Potato healthy',
           'Raspberry healthy',
           'Soybean healthy',
           'Squash Powdery mildew',
           'Strawberry Leaf scorch',
           'Strawberry healthy',
           'Tomato Bacterial spot',
           'Tomato Early blight',
           'Tomato Late blight',
           'Tomato Leaf Mold',
           'Tomato Septoria leaf spot',
           'Tomato Spider mites Two-spotted spider mite',
           'Tomato Target Spot',
           'Tomato Yellow Leaf Curl Virus',
           'Tomato mosaic virus',
           'Tomato healthy']
#
# model = load_model('model.hdf5')
#
#
def convert_and_save(b64_string):
    with open("imagereceivde.png", "wb") as fh:
        fh.write(base64.decodebytes(b64_string.encode()))

# path = 'dataset/val/Grape___Black_rot/0b11ac6a-9e24-4d9c-bbe9-e180eb81ff38___FAM_B.Rot 0581.JPG'
# img = image.load_img(path, target_size=(224, 224))
# img = image.img_to_array(img)
# img = np.expand_dims(img, axis=0)
# img = img / 255
# with graph.as_default():
#     prediction = model.predict(img)
# prediction_flatten = prediction.flatten()
# max_val_index = np.argmax(prediction_flatten)
# print("--------------------")
# print(max_val_index)
# print("--------------------")
# print("Predicted class : " + classes[max_val_index])
# print("Confidence : " + str(prediction_flatten[max_val_index] * 100))
#
# exit()

@app.route('/get/disease', methods=['POST'])
def getDisease():
    if request.method == 'POST':
        imagefile = request.files['image']
        filename = werkzeug.utils.secure_filename(imagefile.filename)
        # filename=filename.split('.')
        # print("\nReceived image File name : " + imagefile.filename)

        path = 'images/IMG_' + str(datetime.now()).replace(" ", "") + '.jpg'
        imagefile.save(path)
        model = load_model('model.hdf5')
        img = image.load_img(path, target_size=(224, 224))
        img = image.img_to_array(img)
        img = np.expand_dims(img, axis=0)
        img = img / 255
        prediction = model.predict(img)
        prediction_flatten = prediction.flatten()
        max_val_index = np.argmax(prediction_flatten)
        print("Predicted class : " + classes[max_val_index])
        print("Confidence : " + str(prediction_flatten[max_val_index] * 100))
        return classes[max_val_index]
    else:
        return 'error'


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True, threaded=True)

    # myfile = request.FILES['plantImage']
    # b64_img = base64.b64encode(myfile.file.read()).decode('UTF-8')
    # img = image.load_img(myfile, img_to_arrayget_size=(224, 224))
    # img = image.img_to_array(img)
    # img = np.expand_dims(img, axis=0)
    # img = img/255
