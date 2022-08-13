package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.parser.nodes.Sequence;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/13 19:28
 * @desc 模型拦截器, 模型每次修正前后都会进行拦截
 */
public interface ModelInterceptor {
    
    /**
     * 前置拦截器
     *
     * @param parseSuccess 修正文本是否解析成功
     * @param model        修正前模型
     * @param proofread    修正文本解析成功后的 节点树
     * @return 返回成功则进行修正, 返回失败则进行修正
     */
    boolean before(boolean parseSuccess, Model model, Sequence proofread);
    
    /**
     * 后置拦截器
     *
     * @param model 修正后的模型
     */
    void after(Model model);
}
