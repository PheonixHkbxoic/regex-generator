# 正则字符分级

```mermaid
graph BT
    subgraph Full
        all["\s\S"]
    end
    blank1-->|是换行|all
    dot-->all

    subgraph High
        dot["."]
    end
    blank1-->|不是换行|dot
    word-->dot
    extend-->dot
    other-->dot

    subgraph Middle
        word["\w"]
    end
    lower1--> word
    upper1--> word
    digit1--> word
    underscore--> word
    chinese1--> word

    subgraph Low
        blank1["\s"]
        lower1["a-z"]
        upper1["A-Z"]
        digit1["\d"]
        chinese1["\u4e00-\u9fa5"]
    end
    blank--> blank1
    lower--> lower1
    upper--> upper1
    digit--> digit1
    chinese-->chinese1

   subgraph Accurate
    blank[空白字符]
    lower[小写字母]
    upper[大写字母]
    digit[数字]
    underscore[下划线]
    extend[扩展ASCII]
    chinese[中文]
    other[其它]
   end
```
