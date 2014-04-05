#define true 1
#define false 0

#define CLIENT_SENDER "/tmp/ldsender"

#define CONNECTION_REQUESTER "/tmp/ldconnecter"
#define CONNECTION_PROVIDER "/tmp/ldanswerer"

#define CONNECTION_OPEN_TOKEN   "::#requestConnectionToLD!553345"
#define CONNECTION_CLOSE_TOKEN  "::#finishConnectionToLD!553345"
#define CONNECTION_OPEN_SUCCESS "::#conSucceedToLD!553345"
#define CONNECTION_OPEN_FAILURE "::#conFailedToLD!553345"

#define MAX_USERNAME_SIZE 64
#define MAX_MESSAGE_SIZE 256

int sender, receiver, requester, provider, msglocker;

typedef struct
{
    char username[MAX_USERNAME_SIZE];
    char message [MAX_MESSAGE_SIZE];

} ChatMessage;

ChatMessage
ReadChatMessage ( int _pipe )
{
    ChatMessage new;
    
    strcpy ( new.username, "\0" );
    strcpy ( new.message, "\0" );
    
    read(_pipe, new.username, MAX_USERNAME_SIZE);
    read(_pipe, new.message, MAX_MESSAGE_SIZE);
        
    return new;
}

void
SendChatMessage ( int _pipe, ChatMessage _m )
{
    write ( _pipe, _m.username, MAX_USERNAME_SIZE );
    write ( _pipe, _m.message, MAX_MESSAGE_SIZE );
}

void
PrintChatMessage ( ChatMessage _m )
{
    printf("%s: %s\n", _m.username, _m.message);
}

/**
 * Verify if message typed is directed to only one user
 */
int
IsPrivateMessage ( ChatMessage _m )
{
    return _m.message[0] == '@';
}

/**
 * Get username related with private message
 */
char *
GetPrivateUsername ( ChatMessage _m, char * _username )
{
    int i = 1;
    
    while ( _m.message[i] != ' ' )
    {
        _username[i - 1] = _m.message[i];
        i++;
    }
    
    _username[i - 1] = '\0';
    
    printf ("Private username found: %s.\n", _username);
    return _username;
}
