package com.busilinq.casepocket.factory;

import com.busilinq.casepocket.base.BaseFragment;
import com.busilinq.casepocket.ui.fragment.CaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 描述：Fragment工厂类
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public class FragmentFactory {

    private static Map<String, BaseFragment> fragmentList = new HashMap<>();

    /**
     * 根据Class创建Fragment
     *
     * @param clazz the Fragment of create
     * @return
     */
    public static BaseFragment createFragment(Class<?> clazz, boolean isObtain) {
        BaseFragment resultFragment = null;
        String className = clazz.getName();
        if (fragmentList.containsKey(className)) {
            resultFragment = fragmentList.get(className);
        } else {
            try {
                try {
                    resultFragment = (BaseFragment) Class.forName(className).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (isObtain)
                fragmentList.put(className, resultFragment);
        }

        return resultFragment;
    }

    public static BaseFragment createFragment(Class<?> clazz) {
        return createFragment(clazz, true);
    }

    public static List<BaseFragment> getFragments() {
        Iterator<BaseFragment> ita = fragmentList.values().iterator();
        List<BaseFragment> list = new ArrayList<>();
        while (ita.hasNext()) {
            list.add(ita.next());
        }
        return list;
    }

    private  static CaseFragment caseFragment = null;
    public static CaseFragment getInstance() {
        if (caseFragment == null)
            caseFragment = new CaseFragment();
        return caseFragment;
    }

}
