# Mi-Se

[![License](http://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/XiaoMiSum/MiGoo/blob/master/LICENSE)
[![MiGoo Author](https://img.shields.io/badge/Author-xiaomi-yellow.svg)](https://maven-badges.herokuapp.com/maven-central/xyz.migoo/migoo)
## 1. 介绍

Mi-Se 是一个Java语言开发的测试工具，可用于Web-UI测试脚本开发。
    
**核心特性**

1.数据 + 关键字驱动，采用 YAML/JSON 描述测试场景，保障测试用例描述的统一性和可维护性；

2.支持扩展函数和自定义变量，可实现复杂的业务逻辑；

3.支持测试前后的环境准备和数据清理；

4.丰富的校验机制，包括：一致(忽略大小写)、不一致、大于、大于等于、小于、小于等于、包含、不包含、空、非空、正则表达式等校验机制，并支持自定义扩展；

5.简洁明了的测试报告与执行日志；

6.支持jenkins持续集成（通过maven运行junit测试类）；

