package com.yanerwu.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author: Zuz
 * @Description:
 * @Date: 2017/4/27 17:26
 */
public class Tools {
    private static final Logger logger = LogManager.getLogger(Tools.class);
    private static String SPACE = "   ";

    /**
     * 获取类所有属性名
     *
     * @param cls
     * @param exclude
     * @return
     */
    public static String getFieldName(Class<?> cls, String... exclude) {
        StringBuffer sb = new StringBuffer();
        List<String> excDic = new ArrayList();
        if (null != exclude && exclude.length > 0) {
            excDic = Arrays.asList(exclude);
        }
        java.lang.reflect.Field[] fields = cls.getDeclaredFields();
        for (java.lang.reflect.Field f : fields) {
            String name = f.getName();
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            if (excDic.contains(name)) {
                continue;
            }
            sb.append(String.format("%s,", name));
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * 深度克隆 必须实现 Serializable
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T clone(T obj) {
        T clonedObj = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObj = (T) ois.readObject();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clonedObj;
    }

    public static String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
            b.append(",");
        }
    }

    /**
     * 可以用于判断 Map,Collection,String,Array是否为空
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (o == null) return true;

        if (o instanceof String) {
            if (((String) o).length() == 0) {
                return true;
            }
        } else if (o instanceof Collection) {
            if (((Collection) o).isEmpty()) {
                return true;
            }
        } else if (o.getClass().isArray()) {
            if (Array.getLength(o) == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).isEmpty()) {
                return true;
            }
        } else {
            return false;
        }

        return false;
    }

    /**
     * 可以用于判断 Map,Collection,String,Array是否不为空
     *
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Object c) throws IllegalArgumentException {
        return !isEmpty(c);
    }

    /**
     * <p>
     * URLDecoder 解码地址
     * </p>
     *
     * @param url 解码地址
     * @return
     */
    public static String decodeURL(String url, String encoding) {
        if (url == null) {
            return null;
        }
        String retUrl = url;

        try {
            for (int i = 0; i < 3; i++) {
                retUrl = URLDecoder.decode(retUrl, encoding);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return retUrl;
    }

    /**
     * url 参数值获取
     *
     * @param url
     * @param name
     * @return
     */
    public static String getUrlParamsByName(String url, String name) {
        String retStr = "";
        if (StringUtils.isNotBlank(url) && url.contains("?")) {
            Map<String, String> map = new HashMap<>();
            String[] paramsArrays = url.split("\\?")[1].split("&");
            for (String pstr : paramsArrays) {
                String[] s = pstr.split("=");
                map.put(s[0], decodeURL(s[1], "utf-8"));
            }
            retStr = map.get(name);
        }
        return retStr;
    }

    public static String listToStr(List<?> list, String s) {
        StringBuffer sb = new StringBuffer();
        try {
            for (Object l : list) {
                sb.append(sb);
                sb.append(",");
            }
            if (list.size() > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String replaceToNull(String str, String... repStr) {
        String result = str;
        for (String r : repStr) {
            result.replace(r, "");
        }
        return str;
    }

    /**
     * 利用MD5进行加密
     *
     * @param str 待加密的字符串
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException     没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public static String encoderMd5(String str) {
        String newstr = str;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newstr;
    }

    /**
     * 删除html标签
     *
     * @param inputString
     * @return
     */
    public static String delHtmlText(String inputString) {
        if (StringUtils.isBlank(inputString)) {
            return "";
        }
        String htmlStr = inputString.trim(); // 含html标签的字符串
        String textStr = "";

        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Pattern p_html1;
        java.util.regex.Matcher m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

            String regEx_html1 = "\\[[^\\]]*\\]";


            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            htmlStr = p_html1.matcher(htmlStr).replaceAll("");

			/* 空格 —— */
            // p_html = Pattern.compile("\\ ", Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = htmlStr.replaceAll("&nbsp;", " ");
            htmlStr = htmlStr.replaceAll(" ", " ");
            htmlStr = htmlStr.replaceAll("\r|\n|\t", "").replaceAll("\"", "“").replaceAll("\'", "‘");
            textStr = htmlStr.trim();

        } catch (Exception e) {
        }
        return textStr;
    }

    public static void main(String[] args) {
        String html = " readx();  宇宙，漆黑而又璀璨。　　　　    这不矛盾，说其璀璨，是因在这无垠的宇宙中，存在了无数如种子般的世界，那一个个世界散出的光，使得这片星空远远一看，璀璨如同一颗颗明珠。　　　　    而说其漆黑，是因这片宇宙实在太大太大了，大到若是走进去，两个种子世界之间的距离，堪称无尽，而在这无尽中存在的，不是璀璨，而是死一般的寂静与漆黑。　　　　    仿佛时光在这里都不再显露，又或者哪怕祖境的力量，似乎也难以将这宇宙淹没。　　　　    不知过去了多久，直至有一天，有一道长虹，以一种惊人的度，从这片宇宙内，从其深处，呼啸而去，刹那间就扫过四方，直奔远处。　　　　    仔细一看，可以看到，在那长虹内，赫然有一个身影，这身影是一个中年男子，似神魂都处于虚弱中。　　　　    这男子穿着一身青色的长袍，面色苍白，仿佛在逃避着什么，目中阴沉，可在其目中深处，却藏着一抹狡诈。　　　　    可就在这中年男子疾驰时，他的身后，原本漆黑的宇宙，瞬间又出现了第二道长虹，那是一个女子，这女子面色苍白，展开全，与那男子一前一后，都在逃遁。　　　　    这二人显然是认识的，可彼此的关系却似乎并非和睦，而是处于某种敌对，在这逃遁中，有数次他们是相互在斗，似都想用各自的方法，让对方度慢下来，但那女子多次处于下风，可虽然这样，但中年男子想要彻底阻挡对方的度，似乎也是不可能。　　　　    呼啸之声打破了宇宙的寂静，在这男女二人的两道长虹之后，很快的，赫然出现了第三道长虹，这道长虹气势磅礴，刚一出现立刻让星空轰鸣，那是一只……　　　　    鹦鹉！　　　　    这鹦鹉羽毛光滑，如有流光在上游走，气势如虹，如同一把绝世战兵，此刻呼啸间，追杀那男女二人。　　　　    说时迟那时快，眨眼间，鹦鹉化作的长虹就蓦然追近，光芒一闪，它的身影度暴增，直奔男女二人而去，刹那临近，巨响滔天，一次随意的撞击，立刻让那女子鲜血喷出，而那中年男子，一样嘴角溢出鲜血。　　　　    “五道，你不要欺人太甚，你家帝君与我本体的如今的这一战，未必能赢，我是仙，宇宙之仙！”中年男子目中微不可查的一闪，可面部却是如歇斯底里一样。　　　　    至于那女子，面色苍白，目中露出苦涩，咬牙疾驰。　　　　    在这逃遁中，很快的，他们的前方出现了一个种子世界，这是宇宙中众多的种子世界之一，只不过这个世界有些青涩，显然是其内的星空意志，还处于成长当中，并未完全诞生出来，而其内的世界中的众生，应该也是尚未开化。　　　　    与此同时，在那男女二人身后追击而来的鹦鹉，此刻冷漠的声音回荡。　　　　    “我家主人斩杀你本尊，势在必得，而我奉主人之命，斩杀你这具被斩开试图留下再起之种的分身，也一样势在必得，还有你，古仙之灵，今日你们二位，逃不掉！”那鹦鹉神色内露出冷漠萧杀之意，话语冰寒，传出时四周八方的宇宙，仿佛置身隆冬冰寒。　　　　    随着话语传出，一个莫大的阵法，一瞬出现在了鹦鹉的四周，笼罩八方，将那男女二人都覆盖在内，不等这二人面色变化，阵法立刻爆出五彩缤纷的光芒，化作了无数个符文，冥冥中有喃喃的诵经之音回荡时，阵法立刻运转，传出滔天杀意，轰轰之声回荡，那一个个符文，同时炸开，形成毁灭一切之力，绝杀男女二人。　　　　    危机关头，那中年男子大吼一声，全身膨胀，不知展开了什么神通，竟化身成为了一个巨人，手中出现了一把巨大的战斧，向着排山倒海般呼啸翻滚而来的符文之力，拼命一斩。　　　　    轰轰之声滔天，那中年男子化作的巨人，鲜血喷出，全身不断崩溃，可却狞笑的大吼。　　　　    “五道，你欺人太甚！！”他话语说出时，赫然从其不断崩溃的体内，居然飞出了十滴金色的血液，这十滴血液刚一出现，立刻凝聚在一起，赫然化作了一个巨大的血色手掌。　　　　    在那手掌内，散出恐怖的气息，这气息使得那鹦鹉也都双目一凝，它立刻认出，这十滴金色的血液，正是自家主人此番试图斩杀罗天本尊的目的所在，那是……宇宙血！　　　　    与此同时，罗天目中那微不可查的光再次一闪，大笑起来。　　　　    “古仙灵，我们之前的约定，到了你去执行的时候了，此番事了，你就自由了！”　　　　    那女子只沉默了一瞬，就下了决断，她面色苍白，可却狠狠一咬牙，她的身体立刻出现了无数的鳞片，双腿如蜕化，竟成了一个半蛇半人的存在，转身不再逃遁，双手抬起，向上狠狠一撑，立刻她全身干瘪下来，那是以生命为代价的神通道法。　　　　    在施展之后，立刻有阵阵浓郁的气息，轰然爆。　　　　    这气息很奇怪，带着沧桑，蕴含了岁月之感，在出现后，猛的扩散，直奔鹦鹉而去，更是压制四周的阵法。　　　　    使得阵法眨眼出现枯萎的征兆，甚至让那鹦鹉的身体，都为之一顿。　　　　    鹦鹉目中露出冷芒，全身气势崛起，强行冲出时，中年男子双手掐诀，立刻他面前十滴鲜血化作的手掌，在这一瞬，一样被推出，那手掌不断变大，到了最后几乎化作一片血海，轰轰而去。　　　　    而那女子，此刻虚弱，施展了那岁月气息的手段后，趁机快后退。　　　　    “五道，你当真以为本仙尊没有任何准备么，你当真以为，是你来追杀么我，此番在这里，正是本尊的计划之一，只要抹去了你的存在，你家帝君，就如断了一臂！”罗天仰天大笑，笑声传出时，他之前的虚弱，在这一刻竟瞬间改变，仿佛方才的所有都是假象，他的目的就是要引对方追杀到这距离宇宙深处极为遥远的区域，在这里，与那女子一起，灭杀对方。　　　　    轰鸣间，那片血海之力磅礴到了极致，化作了封印，眼看就要笼罩鹦鹉时，鹦鹉的目中居然没有丝毫慌乱，而是平静如水。　　　　    “你设局引我到来，却不知我为了我家主人可以顺利斩杀你的本尊，愿意付出一切代价，不知你是在算计我，还是我在算计你。”鹦鹉轻叹，目中露出一丝不舍，可却很快果决，几乎在那血海来临的瞬间，它的体内居然在这一瞬，爆出了一股同样恐怖的波动，那是……自爆的波动。　　　　    它赫然选择了哪怕自爆，也要将对方绝杀在这里的道路。　　　　    若是寻常自爆，或许还无法做到散出如此恐怖的气息，可显然鹦鹉在追杀来此前，已有了决断，展开了某种霸道的手段，使得自身在这一瞬自爆时，可以释放出越自身之力。　　　　    轰鸣在这一刻滔天而起，中年男子双目收缩，可却依旧冷笑一声。　　　　    “你果然是这么选择，不过，本尊准备的，可不止这些。”他话语一出，双手掐诀再次一指，立刻那后退试图离开这里的女子，她的身体猛地一颤，如体内有某种禁制爆，与此同时，那血海翻滚，竟将她也笼罩在内，甚至在这一瞬，这女子的体内，如被动的引出了自爆的波动。　　　　    赫然是这中年男子，操控这女子自爆，来试图阻挡，换来自身一丝生机，而他自身，此刻以极快的度，快后退。　　　　    那女子愤怒，猛的看向中年男子，对方没有遵守约定，在这关键时刻选择牺牲她这里，此事让这女子眼中血丝弥漫，嘴角若仔细看，还可以看到一丝惨笑。　　　　    就在这激烈的双方自爆，就要展开的瞬间，鹦鹉的声音，带着一如既往的冷漠，回荡八方。　　　　    “古仙灵，你还在迟疑什么，我之前和你说过的结果，现在已出现，你还不出手，更待何时！你要的自由，只有你自己去争取，才可以获得。”　　　　    在鹦鹉话语传出的瞬间，中年男子神色次大变。　　　　    而那女子，则是惨笑中深吸口气，这一战前，罗天对她有承诺，而鹦鹉那里也暗中与她有过接触，此刻她眼看一切事情的生，再没有迟疑，出凄厉之笑。　　　　    “你说得对，自由，只有自己来争取，舍去一身古仙位，换来永生自由！”　　　　    中年男子面色变化，隐隐不安，正要开口时，已来不及了，那女子在话语传出后，她的身体居然在这一瞬，轰然瓦解！　　　　    那不是自爆，而是瓦解，无数的血肉，在这一刻从她干瘪的身体内，轰轰碎开，向着四周以穿梭一切的度，刹那扩散，使得这一片星空看起来，如成为了血红。　　　　    “涅槃咒，生生死死，死死生生，让那尘归尘，土归土，让那意识沉沦，让那神智磨灭，让那一切的一切，从此……重新再来！”　　　　    “从此世间再无古仙……”女子的声音传出后，一声震动八方星空的轰鸣巨响，在这一瞬于此次，轰轰爆开。　　　　    一股碾压之力，瞬间降临，直接将鹦鹉碾压成为飞灰的同时，也将那血海碾压，至于那中年男子，他出凄厉的惨叫，身体试图逃走，可却做不到，眨眼间，也被碾压，连同那女子自身，都尽数在这一刻，轰轰碎灭，但却不是死亡，魂飞魄不散。　　　　    因为这女子施展的最后之法，如同开创一个纪元，那是粉碎一切，那是毁灭一切，那是推倒重来。　　　　    而在战场旁边的种子般的世界，也在这碾压的力量下被波及，其内的星空意志，瞬间颤抖，直接被抹去了大半，而这种子世界，也被打开了一个缺口。　　　　    几乎在这缺口出现的一瞬，外面的鹦鹉，中年男子与那女子被碾压成为飞灰融入的气血，如被吸撤一样，眨眼就顺着缺口，直接融入到了这片种子世界内。　　　　    若干年后，在这片种子世界内的星空中，于那无数的众生之内，多出了一个从虚无中凝聚出的鹦鹉之魂，在这世界内茫然的游荡。　　　　    同时，也多出了一个逐渐取代了原本此地星空意志的神魂，忘记了自己的过去，只剩下了本能的罗天意志。　　　　    还有，在那芸芸众生中，也转世轮回般出现了一个女子，她不知道自己的前世，在这星空内，一次又一次的转世，经历了无数的人生，直至无数纪元后，鹦鹉遇到了一个人，那个人为它塑造了一个法器，那是一个铜镜。　　　　    而这女子，也在之后的一天，在这片星空内，无数小世界中，一个叫做至尊仙界的地方，遇到了一个人。　　　　    那个人，被那片小世界的众生，称之为……雷帝。　　　　    那个叫做雷帝的男子，这一生爱上了一个女子，那是他的妻子，在那场至尊仙界的下界叛乱，罗天之力影响的死亡之力入侵时，他为了守护家园，也为了守护这个女子，直至战死。　　　　    他的死亡，化作了无数的雷霆，轰鸣了八方。　　　　    在他死亡后，那个女子呆呆的看着他的尸体，出了悲伤到魂的凄哭，在那哀伤中，女子被封印无数年的记忆，苏醒了，她默默的望着雷帝，眼泪落在了雷帝的铠甲上，许久许久，眼泪消失时，仿佛她对雷帝的情，随着那滴眼泪分离出了自己的体内，她的目中露出了冷漠，还有绝然。　　　　    “我们终究不是一个世界之人，这里只是我这一生旅途中一处短暂停留的地方，现在，我苏醒了，终也该到了我离去的时候，我……自由了。”女子喃喃，带着冷漠，转身离去，走出星空，走向宇宙。　　　　    这里的罗天意志，这里的鹦鹉，她不想相遇，也不想再看到。　　　　    直至她走后，雷帝的尸体漂浮在至尊仙界内的星空中，逐渐的枯萎，可他身上的那套铠甲，却渐渐软化，最终处于半透明的状态，这一切，来自于那女子在记忆苏醒的那一瞬，融合前世今生之力，落下的那滴眼泪。　　　　    这滴眼泪，使得这铠甲似乎凝聚了魂，凝聚了原本应该死亡的雷帝的一丝魂。　　　　    当雷帝的尸体彻底成为飞灰后，这铠甲融合在了一起，在若干年后，诞生了意识，它在意识诞生的一刻就明白，自己这一生，不死不灭，可却并非绝对，它再也不能变成铠甲，或者说，再也不能去舍命保护什么人。　　　　    因为一旦他再去这么做，不死不灭之力，会消散的。　　　　    除了这些，它不记得所有的记忆，一片空白，在这山海界内游荡，渐渐现自己喜欢絮叨，渐渐现自己喜欢教化恶霸，渐渐现自己不会数数……　　　　    直至有一天，它遇到了一个铜镜内飞出的鹦鹉……　　　　    “嗬哟，好大一只铠甲怪，来来来，让五爷仔细看看，怎么没毛呢？”　　　　    “滚滚滚，你这个恶霸，老夫来度化你！”  ";

        long l = System.currentTimeMillis();
        String s = delHtmlText(html);
//        for (int i = 0; i < 10; i++) {
//            s = s.replace("  ", " ");
//        }
        s=s.replace("　　　　", "</p>");
        System.out.println(s);
        System.out.println(System.currentTimeMillis() - l);

    }
}
