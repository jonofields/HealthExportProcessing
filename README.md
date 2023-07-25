# Apple Health Export Processing

***
### About
A hobby project for getting comfortable with Spark
and Scala.  Currently only pushes data to GCP, though planning to add AWS as well.

Final goal is a UI that can return a more user-friendly file type
with aggregations the user can select.
### Pre-requisites
- Apple Health XML file has been downloaded and added 
to the `/data` directory of your project's root directory (Health App -> Upper Right Phote -> Export All)
- An `application.conf` file in the `main/scala/` directory that defines GCS credentials.
- A trait GCSVariables that includes the name of your GCS project and bucket.


