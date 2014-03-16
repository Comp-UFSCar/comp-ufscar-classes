/* 
 * File:   Interpretador.cpp
 * Author: lucas
 * 
 * Created on October 7, 2012, 2:53 PM
 */

#include "Interpretador.h"
#include <string.h>
#include <iostream>

using namespace std;

Interpretador::Interpretador() {
    DefSeqInvalidas();
}

Interpretador::Interpretador(char *pSeq) {
    DefSeqInvalidas();
    AtualizarSeq(pSeq);
}

Interpretador::~Interpretador() {
    for(int i = 0; i < NumSeqInvalidas; i++)
        delete [] SeqInvalidas[i];
    
    delete [] SeqInvalidas;
}

bool Interpretador::AtualizarSeq(char *pSeq) {
    SeqOriginal[0] = SeqMapeada[0] = '\0';
    
    if(strlen(pSeq) > 31) {
        cout << "Erro! Cadeia inserida maior que 31 caracteres!" << endl;
        return false;
    }
    
    strcpy(SeqOriginal, pSeq);
    SeqOriginal[strlen(SeqOriginal)] = '\0';
    
    MapearSeq();
    
    return true;
}

/**
 * MapearSeq() percorre SeqOriginal[] e guarda valores em 
 * SeqMapeada[] segundo a legenda abaixo:
 * 
 * N- Numero de 0 a 9
 * S- Sinal + ou -
 * V- virgula ,
 * P- ponto .
 * ?- qualquer outro que nao os citados acima
 */
void Interpretador::MapearSeq() {
    int i = 0;
    
    while(SeqOriginal[i] != '\0') {
        if(SeqOriginal[i] == '+' || SeqOriginal[i] == '-')
            SeqMapeada[i] = 'S';
        
        else if(SeqOriginal[i] == ',')
            SeqMapeada[i] = 'V';
        else if(SeqOriginal[i] == '.')
            SeqMapeada[i] = 'P';
        
        else if(SeqOriginal[i] >= '0' && SeqOriginal[i] <= '9')
            SeqMapeada[i] = 'N';
        
        else
            SeqMapeada[i] = '?';
        
        i++;
    }
    
    SeqMapeada[i] = '\0';
}

/**
 * Guarda em SeqInvalidas[] todas as sequencias de caracteres que,
 * quando pertencem a SeqMapeada[], tornam esta sequencia invalida.
 
 * === Legenda dos caracteres inseridos no vetor SeqInvalidas ===
 * N: numeral
 * S: sinal
 * P: ponto
 * V: virgula
 * *: Irrelevancia da distancia entre dois caracteres especificos. Ex: "V*P"
 * !: sub-cadeia invalida SE, E SOMENTE SE, esta localizada no inicio ou fim da sequencia completa
 */
void Interpretador::DefSeqInvalidas() {
    
    NumSeqInvalidas = 25;
    
    SeqInvalidas = new char *[NumSeqInvalidas];
    for (int i = 0; i < NumSeqInvalidas; i++)
        SeqInvalidas[i] = new char[10];
    
    strcpy(SeqInvalidas[0], "NNNN*V"); // 1000000
    strcpy(SeqInvalidas[1], "NS");   // 102+203
    
    strcpy(SeqInvalidas[2], "VV");   // 10,,02
    strcpy(SeqInvalidas[3], "VP");   // 234,.202
    strcpy(SeqInvalidas[4], "VS");   // 323,+
    
    strcpy(SeqInvalidas[5], "SS");   // +-1322
    strcpy(SeqInvalidas[6], "SP");   // +.303
    strcpy(SeqInvalidas[7], "SV");   // -,303
    
    strcpy(SeqInvalidas[8],  "PP");  // 201..212
    strcpy(SeqInvalidas[9],  "PS");  // 212.+21
    strcpy(SeqInvalidas[10], "PV");  // 212.,21
    strcpy(SeqInvalidas[11], "PS");  // 212.,21
    strcpy(SeqInvalidas[12], "PP");  // 212..21
    
    strcpy(SeqInvalidas[13], "PNNV"); // 212.21,
    strcpy(SeqInvalidas[14], "PNNP"); // 2.12.
    strcpy(SeqInvalidas[15], "PNV");  // 212.2    
    strcpy(SeqInvalidas[16], "PNP");  // 21.2.5
    
    strcpy(SeqInvalidas[17], "?");   // 331Ade5
    
    strcpy(SeqInvalidas[18], "!V");  // ,291
    strcpy(SeqInvalidas[19], "!P");  // .12
   
    strcpy(SeqInvalidas[20], "V!");  //31,
    strcpy(SeqInvalidas[21], "P!");  //135.
    strcpy(SeqInvalidas[22], "PN!"); //11.2
    strcpy(SeqInvalidas[23], "PNN!");//13.23
    
    strcpy(SeqInvalidas[24], "V*P"); //1202,203.00000    
}

void Interpretador::ExibirSeq(int pIdSeq) {
    
    char *SeqDesejada = pIdSeq == 0 ? SeqOriginal : SeqMapeada;

    cout << "Sequencia = {";
    
    for(int i = 0; SeqDesejada[i] != '\0'; i++)
        cout << "[ " << SeqDesejada[i] << " ]";

    cout << '}' << endl;    
}

void Interpretador::ExibirResultado() {
    cout << endl << (AnalisarSeq() ? "Sequencia valida!" : "Sequencia invalida!") << endl;
}

bool Interpretador::AnalisarSeq() {
    
    // Verificando se a sequencia ee nula
    if(strlen(SeqOriginal) == 0) {
        cout << "<< sequencia nula ou nao instanciada >>" << endl;
        return false;
    }
    /*
    // Verificando a seguinte formacao: V[..]P
    char *SubSeq = NULL;
    if((SubSeq = strstr(SeqMapeada, "V")) != NULL)
        if(strstr(SubSeq, "P") != NULL) {
            cout << "<< Erro: V[...]P >>" << endl;
            return false;
        }
    */
    
    char TmpSubSeq[10], TmpSeq[32], *AstAux;
    int TpSeqIndex = 0;
    
    //  verifica se alguma combinacao que pode invalidar a sequencia
    //  esta presente nessa mesma. strstr() verifica isto e retorna um ponteiro
    //  para a primeira posicao da ocorrencia ou NULL caso nao encontre
    for(int i = 0; i < NumSeqInvalidas; i++) {
        strcpy(TmpSubSeq, SeqInvalidas[i]);
        strcpy(TmpSeq, SeqMapeada);
        
        // Inicio das interacoes que verificam caracteres especiais na subsequencia,
        // como * e !, que indicam distancias variaveis ou definicao especifica do posicionamento
        // da sub-sequencia atual
        
        // simbolo * encontrado!
        // ee preciso verificar se a sub-cadeia B se encontra apos a sub-cadeia A em CadeiaMapeada. Para isso,
        // recorta-se o vetor da seguinte forma:
        // ..A..*..B.. >>> INFERE >>> ..A (A foi encontrado)
        // ..A..*..B.. >>> INFERE >>> B.. (B foi encontrado)
        // ..A + B..   >>> INFERE >>> ..AB.. (B foi encontrado)
        if((AstAux = strstr(TmpSubSeq, "*")) != NULL) {
            *AstAux = '\0'; // NAO MEXA NESTE PONTEIRO (exceto ao restaurar TmpSubSeq)
            
            // A esta em seqMapeada e B tambem. NESTA ORDEM
            // A PERTENCE A SEQMAPEADA: ALPHA := strstr(TmpSeq, TmpSubSeq) != NULL
            // B PERTENCE A SEQMAPEADA -APHA: strstr(strstr(TmpSeq, TmpSubSeq), AstAux +sizeof(char)) != NULL)
            if(strstr(TmpSeq, TmpSubSeq) != NULL && strstr(strstr(TmpSeq, TmpSubSeq), AstAux +sizeof(char)) != NULL) {
                *AstAux = '*';
                strcpy(TmpSeq, TmpSubSeq); // gambiarra para que a validacao abaixo (quando TmpSubSeq for procurada
                                           // em TmpSeq funcione)
            }
            
            *AstAux = '*';
        }
        
        // simbolo ! encontrado na primeira posicao! Deve-se verificar ocorrencia de sub-cadeia necessariamente na primeira posicao
        else if(TmpSubSeq[0] == '!') {
            strcpy(TmpSubSeq, (SeqInvalidas[i] +sizeof(char))); //  remove o simbolo ! da string
            //TmpSeq tem uma quantidade de posicoes cortadas (as posicoes validas agora sao de 0 a strlen(TmpSubSeq)
            TmpSeq[strlen(TmpSubSeq)] = '\0';
        }
        
        // simbolo ! encontrado na ultima posicao! Deve-se verificar a ocorrencia de sub-cadeia necessariamente nas ultimas posicoes
        else if(TmpSubSeq[strlen(TmpSubSeq) -1] == '!') {
            TmpSubSeq[strlen(TmpSubSeq) -1] = '\0';     //  remove simbolo ! da string
            
            if(strlen(TmpSeq) > strlen(TmpSubSeq)) {    //  TmpSeq PRECISA ser maior que TmpSubSeq, ou nao pode ser recortada
                TpSeqIndex = 0;

                // TmpSeq tem uma quantidade de posicoes cortadas (de 0 até a posicao referenciada por strlen(TmpSeq) -strlen(TmpSubSeq))
                while(TpSeqIndex < strlen(TmpSubSeq)) {
                    TmpSeq[TpSeqIndex] = TmpSeq[strlen(TmpSeq) -strlen(TmpSubSeq) +TpSeqIndex];
                    TpSeqIndex++;
                }
                
                TmpSeq[TpSeqIndex] = '\0';
            }
        }
        // Fim das interacoes que verificam caracteres especiais na subsequencia,
        
        // a sub-sequencia TmpSubSeq foi encontrada em TmpSeq, o que implica que TmpSeq ee
        // invalida!
        if(strstr(TmpSeq, TmpSubSeq) != NULL) {
            cout << "<< Erro: " << SeqInvalidas[i] << " >> ";
            return false;
        }
    }
    
    //  nenhuma dependencia encontrada, a cadeia ee valida
    return true;
}