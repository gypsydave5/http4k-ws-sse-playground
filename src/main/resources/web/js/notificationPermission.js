export default async function requestPermissionForNotificationsApi() {
    const result = await Notification.requestPermission()
    switch (result) {
        case 'denied': alert('denied notification permission'); break
        case 'granted': new Notification('Permissions', {body: Notification.permission});
    }
}