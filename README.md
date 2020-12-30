# FW-DATASTORE
i have done the assignment in java 

REQUIREMENTS
1. eclipse IDE or any java editor

STEPS:
1.download the source code from here and open it in a editor
2. download the external JSON jar files
3. add the files in your classpath
4. run

WORKING:
1. basic CRD operations on json datastore which is stored in "data.txt" TEXT FILE.
2.  if want, change input values by editing the "data.txt" file

Create:
  A new key-value pair can be added to the data store using the Create operation. 
  The key is always a string - capped at 32chars. The value is always a JSON 
  object - capped at 16KB.  If Create is invoked for an existing key, an 
  appropriate error will be returned.
  
Read:
  A Read operation on a key can be performed by providing the key, and receiving 
  the value in response, as a JSON object.
  
Delete:
  A Delete operation can be performed by providing the key.
  
Additional features:
  Every key supports setting a Time-To-Live property when it is created. This 
  property is optional.  If provided, it will be evaluated as an integer defining 
  the number of seconds the key must be retained in the data store. Once the 
  Time-To-Live for a key has expired, the key will no longer be available for 
  Read or Delete operations.Appropriate error responses will always be returned 
  to a client if it uses the data store in unexpected ways or breaches any limits. 
  The size of the file storing data will never exceed 1GB.
