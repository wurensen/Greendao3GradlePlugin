# Greendao3GradlePlugin
Greendao 3生成代码插件，非增量编译模式下，在macOS上运行，由于文件系统的差异，可能出现生成的DaoSession和DaoMaster内容乱序，
但是在Windows系统上运行，内容是按字母排序。同时，greendao生成plugin支持增量编译，但是该特性生成的代码也会与全量生成的代码不一致。
所以为了解决双平台生成代码不一致，git不便管理的问题，为此基于greendao的plugin，创建了本plugin。
