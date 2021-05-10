from pusher_push_notifications import PushNotifications


def push_not():

    beams_client = PushNotifications(
        instance_id='661a95d4-7499-47b3-9e3f-5a03827fad36',
        secret_key='3300D2F6DE32046A00577E121354FAC46421E5922AFC2E6116D86E3E8BACD5C8',
    )

    response = beams_client.publish_to_interests(
    interests=['hello'],
    publish_body={
        'apns': {
            'aps': {
                'alert': 'Hello!'
            }
        },
        'fcm': {
            'notification': {
                'title': 'Hello',
                'body': 'Hello, World!'
            }
        }
    }
)

    print(response['publishId'])


if __name__ == '__main__':
    push_not()