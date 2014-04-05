/**
- * ECEC 353 - Programming assignment 2
 * 
 * Author:
 * Lucas David - ld492@drexel.edu
 *    
 * Compile as: gcc server.c -o server -lpthread
 * 
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <signal.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <pthread.h>

#include "message.c"

#define MAX_CONNECTIONS_ALLOWED 10
#define DEFAULT_LINK_PREFIX "ldlink"

ChatMessage self;

char sockets[MAX_CONNECTIONS_ALLOWED][MAX_USERNAME_SIZE + 13];

int * connectedUsersCount;

char *
GetSocket ( int index )
{
    return sockets[index];
}

int
GetClientIndex ( char * username )
{
    int i = 0;
    int client = -1;

    char socketName[MAX_USERNAME_SIZE + 13];

    strcpy (socketName, "/tmp/ldlink");
    strcat (socketName, username);

    // finding the client who is asking to be removed
    while ( client == -1 && i < *connectedUsersCount )
    {
        if ( strcmp ( sockets[i], socketName ) == 0 )
            client = i;

        i++;
    }

    return client;
}

int
AddClient ( char * username )
{
    char reader[MAX_USERNAME_SIZE + 19];
    
    if ( *connectedUsersCount >= MAX_CONNECTIONS_ALLOWED )
        return -1;

    strcpy (sockets[*connectedUsersCount], "/tmp/ldlink");
    strcat (sockets[*connectedUsersCount], username);
    
    strcpy (reader, sockets[*connectedUsersCount]);

    if ( mkfifo (reader, 0666) < 0 )
    {
        printf ("Error oppening sockets for client %s.\n", username);
        return -1;
    }
    
    (*connectedUsersCount)++;

    return *connectedUsersCount - 1;
}

int
RemoveClient ( char * username )
{
    int client, i = 0;
    char reader[MAX_USERNAME_SIZE + 19];

    client = GetClientIndex (username);

    // client not found
    if ( client == -1 )
        return false;
    
    // remove client's link
    strcpy (reader, sockets[client]);
    unlink (reader);

    // shift all other sockets
    for ( i = client + 1; i < *connectedUsersCount; i++ )
    {
        strcpy( sockets[i - 1], sockets[i] );
    }

    (*connectedUsersCount)--;

    // client was successfully removed
    return true;
}

static void
ExitHandler ( int signalNumber )
{
    int i;
    char reader[MAX_USERNAME_SIZE + 14];
    
    printf ("Stopping server...\n");

    unlink (CLIENT_SENDER);

    unlink (CONNECTION_REQUESTER);
    unlink (CONNECTION_PROVIDER);

    for ( i = 0; i < *connectedUsersCount; i++ )
    {
        strcpy (reader, sockets[i]);
        unlink (reader);
    }

    exit (0);
}

void *
HandleNewConnections ( void * ptr )
{
    printf ("Connections handler has started. The server is now accepting connection requests.\n");
    
    int i = 1;
    while ( true )
    {
        requester = open (CONNECTION_REQUESTER, O_RDONLY);
        ChatMessage current = ReadChatMessage (requester);
        close (requester);
        
        // invalid message
        if ( strcmp (current.username, "\0") == 0 )
        {
            continue;
        }
        
        // client has requested to enter on chat
        else if ( strcmp (current.message, CONNECTION_OPEN_TOKEN) == 0 )
        {
            int index;

            printf ("%s has requested to join the chat... ", current.username);
            fflush(stdout);

            // if AddClient() was succesful, return pipe name to client
            if ( index = AddClient (current.username) > -1 )
            {
                strcpy (self.message, CONNECTION_OPEN_SUCCESS);
                printf("GRANTED.\n");
            }
            // return faield message otherwise
            else
            {
                strcpy (self.message, CONNECTION_OPEN_FAILURE);
                printf ("DENIED.\n");
            }
            
            fflush(stdout);

            // send answer to client
            provider  = open (CONNECTION_PROVIDER, O_WRONLY);
            SendChatMessage (provider, self);
            close (provider);
        }

        // client has requested to exit the chat
        else if ( strcmp (current.message, CONNECTION_CLOSE_TOKEN) == 0 )
        {
            printf ("%s has left the chat.\n", current.username);
            RemoveClient (current.username);
        }
    }
}

int main ( int argc, char *argv[] )
{
    int i;
    char reader[MAX_USERNAME_SIZE + 14];
    
    pthread_t a;
    pthread_attr_t attrA;
    
    pthread_mutex_t mutex1 = PTHREAD_MUTEX_INITIALIZER;

    strcpy (self.username, "Server");
    
    connectedUsersCount  = ( int * ) malloc (sizeof (int));
    *connectedUsersCount = 0;

    if ( signal(SIGINT, ExitHandler) == SIG_ERR )
    {
        printf ("Server initializing error: cannot catch SIGINT.\n");
        exit   (1);
    }

    if ( mkfifo (CLIENT_SENDER, 0666) < 0 || mkfifo (CONNECTION_REQUESTER, 0666) < 0 || mkfifo (CONNECTION_PROVIDER, 0666) < 0 )
    {
        printf ("Server initializing error: cannot create communication channels.\n");
        exit (1);
    }
    
    printf ("Server has started.\n");
    
    pthread_attr_init(&attrA);
    pthread_create(&a, &attrA, HandleNewConnections, NULL);
    
    while ( true )
    {
        // read the message sent from client
        receiver = open (CLIENT_SENDER, O_RDONLY);
        ChatMessage current = ReadChatMessage (receiver);
        close (receiver);
        
        // if it is a private message, send it to the user
        if ( IsPrivateMessage (current) )
        {
            char username[MAX_USERNAME_SIZE];

            int index = GetClientIndex (GetPrivateUsername (current, username));

            // client not found
            if (index == -1)
                continue;

            strcpy (reader, GetSocket (index));

            int channel = open (reader, O_WRONLY);
            SendChatMessage (channel, current);
            close (channel);
        }
        // writes down received message to all other clients, otherwise
        else
        {
            for ( i = 0; i < *connectedUsersCount; i++ )
            {
                // Do not send message to the person who wrote it
                if ( GetClientIndex (current.username) == i )
                    continue;

                int channel = open (reader, O_WRONLY);
                SendChatMessage (channel, current);
                close (channel);
            }
        }
    }
}
