/* 
 * File:   Engine.h
 * Author: raul
 *
 * Created on June 20, 2012, 11:42 PM
 */

#ifndef ENGINE_H
#define	ENGINE_H
#include <gtkmm-3.0/gtkmm.h>
#include <gtkmm-3.0/gtkmm/stock.h>
#include <time.h>
#include <cstdlib>
#include <string>
#include <fstream>
#include "ImgList.h"

using namespace std;

class Engine {
public:
    Engine();
    Engine(const Engine& orig);
    virtual ~Engine();
    string getCurrentWord();
    int getDifficulty();
    void setDifficulty(int difficulty);
    void setCurrentWord(string currentWord);

    void loadConfig();

    void newGame();
    void loadImage();

    void cleanGrid();
    void fillGrid();
    void setAllVisible();

    // o unico jeito de ter o mesmo callback pra varios botoes
    Gtk::Widget* getChildByName(Glib::ustring name);
    Glib::ustring IntToUString(int iVal);

private:
    // elementos de interface
    Gtk::Window window;
    Gtk::Image img;
    Gtk::Main main;
    Gtk::Layout layout;
    Gtk::Grid grid;
    Gtk::Box box, boxKeyEntry;
    Gtk::Button btnKeyEntry;
    Gtk::Entry keyEntry;

    // menu
    Gtk::Box boxMenu;
    Glib::RefPtr<Gtk::UIManager> ui;
    Glib::RefPtr<Gtk::ActionGroup> actionGroup;
    Glib::RefPtr<Gtk::RadioAction> radioEasy, radioMedium, radioHard;

    ImgList* imgList;
    string currentWord;
    int difficulty;
    int blockWidth, blockHeight, blockCountCol, blockCountRow;


protected:
    // callback dos botoes
    void on_grid_clicked(Glib::ustring data);
    void on_btnKeyEntry_clicked();
    void on_menu_file_new_generic();
    void on_menu_file_quit();
    void on_menu_radio_easy();
    void on_menu_radio_medium();
    void on_menu_radio_hard();
    int runStatus; // 0 - not running ### 1 - running
};

#endif	/* ENGINE_H */

