import requestPermissionForNotificationsApi from './notificationPermission.js'

document.getElementById("notifications-permission")
    .addEventListener("click", requestPermissionForNotificationsApi)

new SharedWorker("/js/listenForEventAndNotifyWorker.js", {
    name: 'listenForEventAndNotifyWorker'
})
