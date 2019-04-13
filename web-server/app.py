from flask import Flask, render_template, request
from werkzeug import secure_filename

UPLOAD_FOLDER = '/usr/src/app/uploads/'

app = Flask(__name__)
@app.route('/')
def index():
    return render_template('index.html')
	
@app.route('/uploader', methods = ['GET', 'POST'])
def uploader():
   if request.method == 'POST':
      f = request.files['file']
      f.save(UPLOAD_FOLDER + secure_filename(f.filename))
      return 'file uploaded successfully'
		
if __name__ == '__main__':
    app.run(host="0.0.0.0")
