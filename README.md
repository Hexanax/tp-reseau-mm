# TP Réseau Matheus et Matthieu

Binome TP programmation réseaux de Matthieu et Matheus

## Config IntelliJ

This short tutorial should help you getting started with running the server and client on IntelliJ

It is important (for now) to always launch the server before launching the clients, else the clients will crash!

### Server

1. Go in the `Server.java` file and locate the main function, there hover on the green play button at the left of the funciton definition and right click then go in **Modify Run Configurations...**

![image](https://user-images.githubusercontent.com/36091631/142040498-3f510993-7587-43be-8a8d-9eccacef9296.png)

2. There add the port number as an argument, if you are unsure of what to use, use `8080`which is what most server like to use.

### Client

1. Go in the `Client.java` file and locate the main function, there hover on the green play button at the left of the funciton definition and right click then go in **Modify Run Configurations...**. (Same procedure as with the server part).

2.In here you must add **2** arguments: the host of your client and the port it will be pointing to. For host you can put any ip address you have access to but we recommend you use `localhost`, then for the point you _must_ use the same port number as the one used for your server, otherwise your client will point to a port with no server!

3. Now last but not least, you must go to **Modify options** (a bit above arguments) and select the **Allow multiple instances** option which will allow us to launch many clients!

![image](https://user-images.githubusercontent.com/36091631/142041234-3b3eda26-ff55-4f7e-8aa7-2e0d172e27d5.png)


