package org.g9project4.config.controllers;

import org.g9project4.menus.Menu;
import org.g9project4.menus.MenuDetail;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface CommonConfig {
    @ModelAttribute("menuCode")
    default String getMenuCode() {

        return "config";
    }

    @ModelAttribute("subMenus")
    default List<MenuDetail> getSubMenus() {
        return Menu.getMenus("config");
    }
}
