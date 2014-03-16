/* 
 * File:   Engine.cpp
 * Author: raul
 * 
 * Created on June 20, 2012, 11:42 PM
 */


#include "Engine.h"

Engine::Engine() {
    imgList = new ImgList("./res/gato.jpg", "gato");
    loadConfig();
    setDifficulty(0);
    box.set_orientation(Gtk::ORIENTATION_VERTICAL);
    boxMenu.set_orientation(Gtk::ORIENTATION_VERTICAL);
    box.set_homogeneous(false);
    boxMenu.set_homogeneous(false);
    window.add(box);
    window.set_position(Gtk::WIN_POS_CENTER);
    box.pack_end(layout, Gtk::PACK_EXPAND_WIDGET);
    layout.put(img, 0, 0);
    layout.put(grid, 0, 0);
    btnKeyEntry.set_label("gtk-ok");
    btnKeyEntry.set_use_stock(true);
    btnKeyEntry.signal_clicked().connect(sigc::mem_fun(*this, &Engine::on_btnKeyEntry_clicked));
    keyEntry.signal_activate().connect(sigc::mem_fun(*this, &Engine::on_btnKeyEntry_clicked));
    boxKeyEntry.pack_start(keyEntry, Gtk::PACK_EXPAND_WIDGET);
    boxKeyEntry.pack_end(btnKeyEntry, Gtk::PACK_SHRINK);
    box.pack_end(boxKeyEntry, Gtk::PACK_SHRINK);
    box.pack_end(boxMenu, Gtk::PACK_SHRINK);
    actionGroup = Gtk::ActionGroup::create();

    // File menu :
    actionGroup->add(Gtk::Action::create("FileMenu", "Arquivo"));
    //Sub-menu.
    actionGroup->add(Gtk::Action::create("FileNew", Gtk::Stock::NEW),
            sigc::mem_fun(*this, &Engine::on_menu_file_new_generic));
    actionGroup->add(Gtk::Action::create("FileQuit", Gtk::Stock::QUIT),
            sigc::mem_fun(*this, &Engine::on_menu_file_quit));

    // Difficulty menu :
    actionGroup->add(Gtk::Action::create("DifficultyMenu", "Dificuldade"));
    Gtk::RadioAction::Group group_userlevel;
    radioEasy = Gtk::RadioAction::create(group_userlevel, "DifficultyEasy", "Facil");
    actionGroup->add(radioEasy,
            sigc::mem_fun(*this, &Engine::on_menu_radio_easy));
    radioMedium = Gtk::RadioAction::create(group_userlevel, "DifficultyMedium", "Medio");
    actionGroup->add(radioMedium,
            sigc::mem_fun(*this, &Engine::on_menu_radio_medium));
    radioHard = Gtk::RadioAction::create(group_userlevel, "DifficultyHard", "Dificil");
    actionGroup->add(radioHard,
            sigc::mem_fun(*this, &Engine::on_menu_radio_hard));

    ui = Gtk::UIManager::create();
    ui->insert_action_group(actionGroup);
    window.add_accel_group(ui->get_accel_group());

    Glib::ustring ui_info =
            "<ui>"
            "  <menubar name='MenuBar'>"
            "    <menu action='FileMenu'>"
            "      <menuitem action='FileNew'/>"
            "      <separator/>"
            "      <menuitem action='FileQuit'/>"
            "    </menu>"
            "    <menu action='DifficultyMenu'>"
            "      <menuitem action='DifficultyEasy'/>"
            "      <menuitem action='DifficultyMedium'/>"
            "      <menuitem action='DifficultyHard'/>"
            "    </menu>"
            "  </menubar>"
            "  <toolbar  name='ToolBar'>"
            "    <toolitem action='FileNew'/>"
            "    <toolitem action='FileQuit'/>"
            "  </toolbar>"
            "</ui>";

    try {
        ui->add_ui_from_string(ui_info);
    } catch (const Glib::Error& ex) {
        std::cerr << "building menus failed: " << ex.what();
    }

    //Get the menubar and toolbar widgets, and add them to a container widget:
    Gtk::Widget* pMenubar = ui->get_widget("/MenuBar");
    if (pMenubar)
        boxMenu.pack_start(*pMenubar, Gtk::PACK_SHRINK);

    Gtk::Widget* pToolbar = ui->get_widget("/ToolBar");
    if (pToolbar)
        boxMenu.pack_start(*pToolbar, Gtk::PACK_SHRINK);
    

    runStatus = 0;
}

Engine::Engine(const Engine& orig) {
}

Engine::~Engine() {
}

string Engine::getCurrentWord() {
    return currentWord;
}

int Engine::getDifficulty() {
    return difficulty;
}

void Engine::setDifficulty(int difficulty) {
    this->difficulty = difficulty;
}

void Engine::setCurrentWord(string currentWord) {
    this->currentWord = currentWord;
}

void Engine::loadConfig() {
    ifstream imgFile;
    imgFile.open("./img.txt");
    if (imgFile.is_open()) {
        string line;
        while (imgFile.good()) {
            getline(imgFile, line);
            if (!line.length()) continue;
            // caracter ' ' (espaco) separa caminho e palavra na linha
            int posSharp = line.find(' ');
            string path = line.substr(0, posSharp);
            string keyword = line.substr(posSharp + 1);
            imgList->add(path, keyword);
            //cout << "path=" << path << " keyword=" << keyword << endl;
        }
        imgFile.close();
    }
}

void Engine::loadImage() {
    srand(time(NULL));
    int randomImgIndex = rand() % (imgList->getCount());
    ImgList* aux = imgList;
    for (int i = 0; i < randomImgIndex; i++) {
        aux = aux->getNext();
    }
    setCurrentWord(aux->getKeyword());
    string randomPath = aux->getPath();
    img.set(randomPath);
}

void Engine::newGame() {
    if (runStatus) {
        cleanGrid();
    }

    loadImage();

    if (!window.get_visible())
        window.show_all();

    switch (getDifficulty()) {
        case 0:
            // facil (3x3)
            blockCountCol = 3;
            blockCountRow = 3;

            break;
        case 1:
            // medio (6x6)
            blockCountCol = 6;
            blockCountRow = 6;
            break;
        case 2:
            // dificil (9x9)
            blockCountCol = 9;
            blockCountRow = 9;
            break;
    }

    int imgWidth = img.get_allocated_width();
    int imgHeight = img.get_allocated_height();
    window.set_size_request(imgWidth, imgHeight + 100);
    window.set_resizable(false);

    blockWidth = imgWidth / blockCountCol;
    blockHeight = imgHeight / blockCountRow;

    fillGrid();

    grid.set_size_request(imgWidth, imgHeight);

    grid.show_all();
    
    keyEntry.grab_focus();
    
    if (runStatus == 0) {
        runStatus = 1;
        Gtk::Main::run(window);
    }
}

void Engine::fillGrid() {
    for (int i = 0; i < blockCountCol; i++) {
        for (int j = 0; j < blockCountRow; j++) {
            grid.attach(*Gtk::manage(new Gtk::Button()), i, j, 1, 1);
            Gtk::Button* btn = (Gtk::Button*)grid.get_child_at(i, j);
            btn->set_name("btn" + IntToUString(i) + IntToUString(j));
            btn->signal_clicked().connect(sigc::bind<Glib::ustring > (sigc::mem_fun(*this, &Engine::on_grid_clicked), "btn" + IntToUString(i) + IntToUString(j)));
            btn->set_size_request(blockWidth, blockHeight);
            btn->set_focus_on_click(false);
        }
    }
}

void Engine::cleanGrid() {
    for (int i = 0; i < blockCountCol; i++) {
        for (int j = 0; j < blockCountRow; j++) {
            Gtk::Widget* widget = grid.get_child_at(i, j);
            grid.remove(*widget);
            delete widget;
        }
    }
}

void Engine::setAllVisible() {
    for (int i = 0; i < blockCountCol; i++) {
        for (int j = 0; j < blockCountRow; j++) {
            Gtk::Widget* widget = grid.get_child_at(i, j);
            widget->set_child_visible(true);
        }
    }
}

void Engine::on_grid_clicked(Glib::ustring data) {
    Gtk::Widget* btn = getChildByName(data);
    cout << data << endl;
    //btn->set_visible(false);
    btn->set_child_visible(false);
}

void Engine::on_btnKeyEntry_clicked() {
    string userTry = keyEntry.get_text();
    cout << userTry << endl;
    int verifyWord = getCurrentWord().compare(userTry);
    if (verifyWord == 0) {
        Gtk::MessageDialog msg("Acertou!!");
        msg.set_secondary_text("Meus parabens, voce e bom mesmo ein!");
        msg.set_modal(true);
        //setAllVisible();
        int dummy = msg.run();
    } else {
        Gtk::MessageDialog msg("Errou!");
        msg.set_secondary_text("Tente novamente");
        msg.set_modal(true);
        int dummy = msg.run();
    }
    keyEntry.set_text("");
    keyEntry.grab_focus();
    // para nao travar no loop da caixa de dialogo
    if (verifyWord == 0) {
        newGame();
    }
}

void Engine::on_menu_file_new_generic() {
    newGame();
}

void Engine::on_menu_file_quit() {
    window.hide();
}

void Engine::on_menu_radio_easy() {
    setDifficulty(0);
    newGame();
}

void Engine::on_menu_radio_medium() {
    setDifficulty(1);
    newGame();
}

void Engine::on_menu_radio_hard() {
    setDifficulty(2);
    newGame();
}

Gtk::Widget* Engine::getChildByName(Glib::ustring name) {
    vector<Gtk::Widget*> v = grid.get_children();
    for (int i = 0; i < v.size(); i++) {
        if (v[i]->get_name() == name) return v[i];
    }
}

Glib::ustring Engine::IntToUString(int iVal) {
    std::ostringstream ssIn;
    ssIn << iVal;
    Glib::ustring strOut = ssIn.str();

    return strOut;
}