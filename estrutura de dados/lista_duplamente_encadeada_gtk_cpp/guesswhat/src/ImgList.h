/* 
 * File:   ImgList.h
 * Author: raul
 *
 * Created on June 20, 2012, 8:35 PM
 */

#ifndef IMGLIST_H
#define	IMGLIST_H
#include <string>
#include <iostream>

using namespace std;

class ImgList {
    
    static int count;
    
public:
    int getCount();
    
    ImgList();
    ImgList(string path, string keyword);
    ImgList(const ImgList& orig);
    virtual ~ImgList();
    
    ImgList* getNext();
    void setNext(ImgList* next);
    ImgList* getPrev();
    void setPrev(ImgList* prev);
    
    string getPath();
    string getKeyword();
    void setPath(string path);
    void setKeyword(string keyword);
    
    void add(string path, string keyword);
    void remove(string keyword);

private:
    ImgList *next,*prev;
    string path, keyword;

};

#endif	/* IMGLIST_H */

