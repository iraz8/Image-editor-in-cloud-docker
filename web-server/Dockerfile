FROM alpine:edge

RUN apk update && apk add bash
RUN apk add --update py2-pip

COPY . /usr/src/app/
COPY templates/ /usr/src/app/templates/
COPY uploads/ /usr/src/app/uploads/
RUN pip install --no-cache-dir -r /usr/src/app/requirements.txt

EXPOSE 5000
CMD ["python", "/usr/src/app/app.py"]
