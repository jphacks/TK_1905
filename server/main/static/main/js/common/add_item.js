$(() => {
    let hided_items = []
    $('.item').each((_, item) => {
        item = $(item)
        const elm = item.find('input').first()
        if (!elm.val()) {
            item.hide()
            hided_items.push(item)
        }
    })
    let add_item = () => {
        if (hided_items.length > 0) {
            hided_items[0].show()
            hided_items.shift()
        } else {
            alert('これ以上追加できません。\n一度送信してください。')
        }
    }
    $('#add_item').on('click', add_item)
    if (hided_items.length > 0) {
        add_item()
    }
})
