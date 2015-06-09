# remote-netty

remote-netty是基于Netty4的一个简便网络框架的封装，主要提供了两种特性：

一、提供长连接的连接池网络模型

二、提供与NIO结合的客户端阻塞读模式，这个简化了客户端的代码，即可以利用NIO的高效特性，又不需要调整代码逻辑

# Server用法

参考 com.lefu.remote.netty.test.IOServerTest

# Client用法

参考测试用例：

com.lefu.remote.netty.test.client.IOClientTest

com.lefu.remote.netty.test.client.IOClientBlockTest

com.lefu.remote.netty.test.client.PooledClientTest

# wiki

https://github.com/leo27lijiang/remote-netty/wiki
