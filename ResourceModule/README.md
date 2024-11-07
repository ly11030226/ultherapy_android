本文为解释具体功能用法
首先 在自己application中oncreate方法中初始化ReadFileUtils，调用ReadFileUtils.init（this）用以提供长期持有的application上下文
然后setColor设置默认app主色调（不设置的话会是工具类默认颜色，不一定好看）
然后如果文件已经存在，则调用读取文件方法读取所需要的文字数据
如果颜色文件存在，则调用读取颜色文件的方法，但是并不一定每次读取颜色文件，因为目前颜色有且仅有一个，
如果后续不想每次都读取文件的话，可以放在shard中然后初始化时获取当前是否使用额外颜色的判断来直接调用setColor方法，就可以全局使用
或者直接放弃本方法，自己持有都是可以的

如果使用获取图片shape方法一定要设置颜色setColor 此方法为变更app主色调，包括图片
图片设置如果是shape则可以在设置颜色后调用，调用方法为getDrawableByShapeUpdateBackground 只改变填充色
                                                 getDrawableByShapeUpdateStroke  只改变边框色，需要填写边框粗细
             selector获取方法(由于这个需要反射，所以目前采用相对简单而且实用的系统创建方法的自己实现)
                    getDrawableBySelector(SelectorDrawableItem... items)
                    参数为不固定个数的  SelectorDrawableItem （至少一个，不然选择器将毫无意义）
                                本实体类有两个属性states和drawableRes
                                        states为一组状态的数组 drawableRes为这一组状态情况下显示的图片资源(如需变色可用上述取shape的方法来取)
                                            返回直接就是图片选择器类型的图片，可直接使用
             selector选择器为color选择时（也是需要反射，所以也是使用的正向创建法）
                    getColorSelector(SelectorColorItem... items)
                    参数也可为不固定个数的 SelectorColorItem （同上方法，也至少一个，不然选择器无意义）
                                本实体类有两个属性 states和color
                                        states为一组状态的数组 color为这一组状态情况下显示的颜色值
                                            返回的是颜色选择器，因为这个不能转成颜色类型，所以只能用setTextColor(ColorStateList colors)来设置颜色

读取文字调用readFile方法，调用此方法时是否启用默认状态会改为否后续就可以使用其中的数据，读取文件可以是任意位置，只要可以读到就可以
        getString(String name) 获取标签name中的名字获取map中的对应字段（如果未获取到会得到空字符串）
        getString(@StringRes int resId) 根据资源id获取map中或者默认string.xml中的对应字段内容
        如果调用此方法前未初始化本工具类，则会返回null
        如果isDefault还为true则会从默认string.xml中取值
        如果isDefault为false则会尝试从map中取，取不到的话会去默认string.xml中取值
clearMap（） map中存放数据不会自动切换，或者消除，如果需要则需要调用此方法清空数据，如果不清空，则会变为追加覆盖数据
getColor（）获取app主色调，需要初始化时设置，不设置的话会返回0xFF1111；
readColor(File file,String colorName) 读取文本颜色 需要file对象和需要读取的颜色标签的name，必须指定
            读取成功后会变更记录的app主色调，并且是否启用默认颜色状态会变为否，返回值为颜色int值(
            返回值改为监听返回，因为需要开辟子线程
            )
