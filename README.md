# edm-message-service-consumer

## More infos at:
https://github.com/edvmorango/event-driven-messenger

## Stack 
__(ZIO + sqs + doobie)__


## Example message payload (will be replaced by __*AsyncApi*__ soon)

```
{
    "uuid": "494a716b-43f9-4206-8611-a6092c11d9d9",
    "message": "ZIO is awesome!",
    "sender": {
        "uuid": "70de8194-0fd0-4c38-b52c-0a161c1f61c8",
        "name": "José E. Vieira M.",
        "email": "sender@gmail.com",
        "birthDate": "1996-06-10"
    },
    "peer": {
        "uuid": "8851c3ce-5e47-4b3a-9557-b88e3c166a39",
        "name": "J. Eduardo V. Morango",
        "email": "peer@gmail.com",
        "birthDate": "1996-06-10"
    },
    "sendDate": "2019-05-12T17:18:07.177468"
}
```
