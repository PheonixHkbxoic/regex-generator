# regex-generator

## 说明

**regex-generator**是一个用于生成自动生成正则表达式的引擎库(regex generator engine).

它的功能是通过多次扫描结构相同或相似文本，提取其结构，构造出**模型**(节点树)并收集相关信息，多次扫描可以更加完善模型，收集更多的信息

## 架构

通过**文本**构造**模型**, 生成器会根据**模型**生成**正则**

```mermaid
graph LR
    T1[文本]--扫描-->L1(词法分析器1)
    T2[文本2]--扫描-->L2(词法分析器2)
    T3[文本3]--扫描-->L3(词法分析器3)


    subgraph 模型构建器
        subgraph 上下文组
        L1-->C1(上下文1)
        L2-->C2(上下文2)
        L3-->C3(上下文3)
        end

        model[(模型)]    
        C1--构造-->model
        C2--修正-->model
        C3--修正-->model

        model--收集-->profile{{元信息 如:长度,次数等}}

    end


    G{生成器}-.依赖.->model
    G-.依赖.->profile
    G-->merger
    subgraph 处理器链
        merger>合成器]
        -->prune>剪枝]
        -->generalize>泛化]
        -->generate>生成]
    end
    generate-->regex>正则]

    用户--第1步-->input(输入)
    input-->T1
    input-->T2
    input-->T3

    用户---第2步-..->GC[配置或默认]-..->G
    用户--第3步-->api(调用api生成正则)-->G
```

## 待办列表

#### 文本解析(模型构建)

- [x] 基本解析

- [ ] 支持更多常见结构，如：URI

- [ ] 支持常用中文PAIR

#### 模型修正(合并)

- [x] 基本修正

- [ ] 支持更多节点，如Id

- [x] 信息收集

#### 正则生成

- [x] 基本生成

- [x] 节点复制

- [ ] 后缀提取

- [x] 节点剪枝

- [x] 节点泛化
