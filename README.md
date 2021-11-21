# Nasa-API-Project
This project is a Server-Client communication and Socket-Programming project that uses Nasa API's for receiving Astronomy Pictures and Mars Weather data.

**** Note: First run the Server.java, then run the Client.java, For API key generation, visit NASA API website: https://api.nasa.gov/ ****

The project is a network application project that performs network connection between client-server-NASA APIs. The application firstly starts to run with server side of the project. After server is up and running, clients can connect to the server. Server can connect multiple clients at the same time which is called multi-threading. After client/s connect to the server, they need to identify themselves entering username and password. Username-password list provided in a text file named “users” with the project. Sample username-password list:

![image](https://user-images.githubusercontent.com/37505916/142761428-e5db825b-7263-42b0-9c3f-fdc499094642.png)

Username could be provided by the command line arguments. But if command line arguments are not provided then client will be asked for their username. If username entered by the user does not exist in the list, connection will be aborted by the server. If username does exist, then password will be required. If client gives a wrong password, server asks client to provide a password 2 more times, after all attempts if still password is wrong, then connection will be aborted by the server again. There is no time-out for providing the username or password etc. In the case client provides both username and password correctly, the connection will be refreshed by the server by providing new socket port to the client. Previous connection is closed by server.

![image](https://user-images.githubusercontent.com/37505916/142761484-1f0b1801-c9c6-422f-b0f5-dcb5427022e8.png)

So far, this phase is called authorization. Username-password identification will be done with simple functions called “verifyUsername” and “verifyPassword”. These functions search the list when they are called.

After new connection is provided, client will be asked to provide a date for server to bring the image from the NASA API called Astronomy Picture of Day. Server takes the date, form a proper URL to connect to the API, extracts the picture URL and downloads it to the local machine. Step by step explanation:

![image](https://user-images.githubusercontent.com/37505916/142761558-3ebb8450-851b-46fe-9b18-7e98472db349.png)

This function assembles the provided date with the API url to extract the picture url. Broad explanation for url settings is in the NASA’s API website. Connection to the NASA’s api is done by Http class in java. The body of this api is in the form of a JSON object. Picture url is the value of the key ‘url’ in this JSON. Url extracted using org.json library in java. Image url is returned as a string. 

In the meantime, data from Insight API is saved and ready for transferring.

![image](https://user-images.githubusercontent.com/37505916/142761575-fa3096e0-eae7-4e88-9bfc-c057130176bc.png)

This insightAPI function performs the connection and extraction data from the api. The returned value is a JSON object which stores the atmospheric pressure data of a randomly selected sol. Again the extraction and parsing the JSON object is done with org.json library. 
Randomization is done by simple function called “getPort” which generates a random number in the specified range. The “getPort” function is also used for creating new ports for connection between client and server. 

![image](https://user-images.githubusercontent.com/37505916/142761606-59a3b06e-7751-4b8f-b324-b68f501b4baa.png)

This simple function downloads the bytes in an url and saves it in a specified location in the machine. 
After download is completed, new data transfer streams are opened both in client and server sides to transfer the image to the client. 
Image file is converted to a byte object that stores the data of image. This byte is transferred to the client and client convert it to an image file back and saves it. 

![image](https://user-images.githubusercontent.com/37505916/142761619-0ea7145b-f8c7-446a-8195-b1bf038aeea9.png)

![image](https://user-images.githubusercontent.com/37505916/142761624-9e4a4492-4bca-45f5-9422-e4873212504a.png)

Before transferring, hash code for image file is generated. First, the file size is sent to the client, image file is transferred and the data from Insight api is transferred to the client. Insight api data transferred as a string, then it is converted to a JSON object to parse it properly in the client side. Hash code for mars data is also created and transferred. 

![image](https://user-images.githubusercontent.com/37505916/142761639-a184366f-5baa-4b99-920a-4134c9659b72.png)

Hash codes for data are printed on the console, the hash code for image file is different which is caused by large file transferring. But hash code for the mars data is the same. I could not dive into hash code debugging because of time issue. 
After all files and data received by the client, client is informed with a message. Until client type “quit” to the console, program does not display the received data. Client can continue to send and receive messages to/from server. 
After client type “quit” to the console, all connections and streams are closed. Received data is displayed on a java GUI program. 

NOTE: org.json library does not included in the default java library so you have to add this library manually to the project. However, there is going to be still an issue when adding external libraries to this project. I am using eclipse, the proper way to adding library is following: At the navigation tab Project>>Properties>>Java Build Path>>Libraries>>Choose Classpath>>Add external JAR’s>>Choose the jar path. 
The design and implementation of this project is not perfect due to time concerns. However, the application is properly working and providing the given tasks. 

Sample run for the application is below.

![image](https://user-images.githubusercontent.com/37505916/142763345-c1181af8-cd62-4135-bb29-1e7f1acf2cd1.png)

![image](https://user-images.githubusercontent.com/37505916/142763394-dcb8b2df-c56a-467f-8ad8-55cd142eda5a.png)
