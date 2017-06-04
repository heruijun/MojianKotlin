package com.hrj.mojian.eventcenter

enum class EventName {
    /**
     * 更新操作栏视图，需要传四个boolean参数，1、显示操作栏，2、显示文字提示，3、显示替换，4、显示移除
     */
    update_operation_bar_view,

    /**
     * 判断某一魔键是否设置功能，需要魔键位置（0，1，2，3，4）
     */
    mj_is_setted,

    /**
     * 清除魔键功能，需要魔键位置
     */
    mj_remove,

    /**
     * 设置魔键功能，需要传两个参数：1、魔键位置，2、被设置的魔键
     */
    mj_set,

    /**
     * 替换魔键功能，参数：1、替换魔键的位置，2、被替换魔键的位置
     */
    mj_replace,

    /**
     * 通过hascode查找MJItem，参数：1、hashcode值
     */
    find_function_item_by_hashcode,

    /**
     * 增加mjBean到首页，参数：mjBean
     */
    add_function_item_to_home,

    /**
     * 移除首页mjBean，参数：mjBean
     */
    remove_function_item_from_home,

    /**
     * 替换首页mjBean，参数：1、被替换的mjBean，2、新的mjBean
     */
    replace_function_item_from_home

}