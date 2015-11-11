# ECEC 353 :: Programming assignment 2

# Author

Lucas David - ld492@drexel.edu

# Compile instructions


1. Confirm that server.c, client.c and message.c are in the same folder.
1. gcc server.c -o server -lpthread
2. gcc client.c -o client -lpthread

# Execution instructions

1. Open a terminal and type ./server and press enter, the server will start to work.
3. Open another terminal and type ./client <<username>>, then press enter. The server will display a message informing the enter of that client.
4. Open more terminals and join new users...
5. Type any message in one of the clients and press enter, the message will be displayed on all other users.
6. Send private messages, start the message with @<<username>>
    Examples:
        @lucasdavid hey lucas!
        @maria I am potato!
        @johnpaul I solved the last issue!
7. Press ctrl+c in one of the clients to exit from the chat. Messages will be displayed to the exit user and to the server.