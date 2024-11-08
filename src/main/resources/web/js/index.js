import testSse  from './sse.js'

import requestPermissionForNotificationsApi from './notificationPermission.js'

testSse('bob')

document.getElementById("notifications-permission")
    .addEventListener("click", requestPermissionForNotificationsApi)

new SharedWorker("/js/listenForEventAndNotifyWorker.js", {
    name: 'listenForEventAndNotifyWorker'
})
