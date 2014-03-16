/*
 * Carta.cpp
 *
 *  Created on: 15/04/2012
 *      Author: Thiago faria
 */
#include "Carta.h"

Carta::Carta(void){
	naipe = 0;
	numero = 0;
	area.x = 0;
	area.y = 0;
	area.w = 106;
	area.h = 167;
}

Carta::Carta(int c, int n){
	naipe = n;
	numero = c;
	area.x = 0;
	area.y = 0;
	area.w = 106;
	area.h = 167;
}

int Carta::getNum(){
	return numero;
}

int Carta::getNaipe(){
	return naipe;
}

int Carta::getX(){
	return area.x;
}

int Carta::getY(){
	return area.y;
}

int Carta::getW(){
	return area.w;
}

int Carta::getH(){
	return area.h;
}

void Carta::Posicionar(int x, int y){
	area.x = x;
	area.y = y;
}

SDL_Rect Carta::getArea(){
	return area;
}

void Carta::setNaipe(int x){
	naipe = x;
}

void Carta::setNum(int x){
	numero = x;
}

void Carta::operator=(Carta &c){
	naipe = c.getNaipe();
	numero = c.getNum();
	area.x = c.getX();
	area.y = c.getY();
}



