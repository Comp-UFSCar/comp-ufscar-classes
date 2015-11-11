/**
 * ECEC 353 - Programming assignment 2
 * 
 * Author:
 * Lucas David - ld492@drexel.edu
 *    
 * Compile as: gcc client.c -o client -lpthread
 * 
 */

#include <stdio.h>  
#include <string.h>
#include <ctype.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <pthread.h>

#include "message.c"

ChatMessage self;

/**
 * Signals the exit of this client to the server
 * 
 * @param signalNumber
 */
static void ExitHandler ( int signalNumber )
{
    // informing the server that this client is leaving
    requester = open (CONNECTION_REQUESTER, O_WRONLY);
    strcpy (self.message, CONNECTION_CLOSE_TOKEN);
    SendChatMessage (requester, self);
    
    close (requester);
    close (sender);
    close (receiver);
    
    printf ("\nBye, %s.\n", self.username);
    exit (0);
}

/**
 * Handle receiving of messages from the server
 *
 */
void * ClientReceiver ( void * ptr )
{
    char receiverSocket [MAX_USERNAME_SIZE + 19];
    
    strcpy (receiverSocket, "/tmp/ldlink");
    strcat (receiverSocket, self.username);
    
    while ( true )
    {
        receiver = open (receiverSocket, O_RDONLY);
        ChatMessage current = ReadChatMessage (receiver);
        close (receiver);
        
        if ( strcmp (current.username, "\0") == 0 )
            continue;

        printf ("<<Self %s>> %s says: %s\n", self.username, current.username, current.message);
    }
}

int main ( int argc, char *argv[] )
{
    // username is not present or is bigger than maximum length allowed
    if ( argc < 2 || strlen (argv[1]) >= MAX_USERNAME_SIZE - 1 )
    {
        printf("Usage: client <string> (maximum of %i characters)\n", MAX_USERNAME_SIZE - 1);
        exit (1);
    }
    
    if ( signal(SIGINT, ExitHandler) == SIG_ERR )
    {
        printf ("Client initializing error: cannot catch SIGINT.\n");
        exit   (1);
    }
    
    // define new username and ask for server's permission to join in the group
    strcpy (self.username, argv[1]);
    strcpy (self.message, CONNECTION_OPEN_TOKEN);
    
    requester = open (CONNECTION_REQUESTER, O_WRONLY);
    SendChatMessage  (requester, self);
    close (requester);
    
    printf ("Join request sent...");
    printf ("\n");
    
    // wait for server response
    provider = open (CONNECTION_PROVIDER, O_RDONLY);
    ChatMessage answer = ReadChatMessage (provider);
    close (provider);
    
    printf("Server replied with %s.\n", answer.message);
    
    // connection failed. Server refused to listen to this client.
    if ( strcmp (answer.message, CONNECTION_OPEN_SUCCESS) != 0 )
    {
        printf ("Client initializing error: cannot establish connection with server because it is currently unavailable or the limit of connections was reached.\n");
        exit (1);
    }
    else
    {
        pthread_t a;
        pthread_attr_t attrA;
        
        // initiate client receiver
        pthread_attr_init (&attrA);
        pthread_create    (&a, &attrA, ClientReceiver, NULL);
        
        // handle messages sent by client
        while ( true )
        {
            fgets  (self.message, sizeof (self.message), stdin);
            
            sender = open (CLIENT_SENDER, O_WRONLY);
            SendChatMessage (sender, self);
            close (sender);
        }
    }
}
