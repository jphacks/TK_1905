import json
import traceback
# from asgiref.sync import AsyncToSync

from channels.generic.websocket import AsyncJsonWebsocketConsumer
# from channels.db import database_sync_to_async

from main.utils import json_serial


class RoomConsumer(AsyncJsonWebsocketConsumer):
    async def connect(self):
        self.room_name = None
        if not self.scope['user'].is_authenticated:
            await self.close()
            return
        try:
            user = self.scope['user']
            room_id = self.scope['url_route']['kwargs']['pk']
        except (KeyError):
            traceback.print_exc()
            await self.close()
            return

        room_name = "room_%s" % room_id
        await self.channel_layer.group_add(room_name, self.channel_name)

        self.user = user
        self.room_name = room_name

        await self.accept()

    async def disconnect(self, close_code):
        if self.room_name:
            await self.channel_layer.group_discard(self.room_name,
                                                   self.channel_name)

    async def receive(self, text_data=None, bytes_data=None):
        await self.channel_layer.group_send(
            self.room_name, {
                'type': 'room_message',
                'message': json.dumps({
                    "content": text_data
                },
                                      default=json_serial)
            })

    async def room_message(self, event):
        message = event['message']
        await self.send(text_data=message)
