/* 
 * File:   main.cpp
 * Author: raul
 *
 * Created on June 20, 2012, 7:44 PM
 */


#include "src/ImgList.h"
#include "src/Engine.h"

// inicializando variavel static
int ImgList::count = 0;

int main(int argc, char *argv[]) {
    // inicializando o gtk
    Glib::RefPtr<Gtk::Application> app =
            Gtk::Application::create(argc, argv,
            "org.guesswhat");
    Engine e;
    e.newGame();
    
}