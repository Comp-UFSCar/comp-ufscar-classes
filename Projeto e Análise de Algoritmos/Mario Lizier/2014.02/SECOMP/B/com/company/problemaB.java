package com.company;

/**
 * Created by Thales on 10/25/2014.
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *  Problema B - Índice Carrasco Mamata
 *
 *      Os estudantes da Universidade Federal dos Senhores Carrascos (UFSCar) passam por uma situação
 *  muito estressante todo início de semestre. Esses alunos precisam se inscrever nas disciplinasOfertadas
 *  que irão cursar e dependendo das escolhas feitas, o semestre pode se tornar excessivamente carrasco.
 *
 *      Para auxiliá-los nessa importante tarefa, os estudantes coletaram alguns dados sobre as disciplinasOfertadas
 *  ofertadas nos anos anteriores e criaram o índice CM. O índice CM é um valor inteiro de 0 a 100, que classifia
 *  as ofertas de disciplinasOfertadas como carrascas, quando próximo de 0, ou mamatas, quando próximo de 100. O
 *  índice CM foi calculado para todas as ofertas de todas as disciplinasOfertadas e agora os alunos procuram se
 *  matricular, prioritariamente, nas disciplinasOfertadas com alto valor de CM, ou seja, nas disciplinasOfertadas mais mamatas.
 *
 *      Em posse da lista de ofertas de disciplinasOfertadas que um aluno pode se matricular no semestre, sua tarefa é
 *  descobrir qual o maior valor acumulado de CM que esse aluno pode conseguir, matriculando-se no maior
 *  número de disciplinasOfertadas possível. O CM acumulado é obtido somando-se todos os índices CM das disciplinasOfertadas
 *  que o aluno se matricula. Cada disciplina é oferecida em um horário fio semanal.
 *
 */
public class problemaB {

    private static LinkedList<Disciplina>[] disciplinasOfertadas = (LinkedList<Disciplina>[]) new LinkedList<?>[7];
    private static LinkedList<Disciplina>[] disciplinasSelecionadas = (LinkedList<Disciplina>[]) new LinkedList<?>[7];
    private static int d = 0, cmAcumulado = 0;

    public static void lerNumOfertas(String n){
        if (Integer.parseInt(n) >= 1 && Integer.parseInt(n) < 80000)
            d = Integer.parseInt(n);
        else
           System.err.print("Numero de ofertas de disciplina fora do range (1 <= D < 80000");
    }

    public static void lerDisciplina(String linha){
        String separadores = "[ ]"; // separa String em tokens quando há espaço
        String[] tokens = linha.split(separadores);
        for (int i = 0; i < tokens.length; i += 4) {
            disciplinasOfertadas[indice(tokens[i])].add(new Disciplina(tokens[i],   // dia da semana
                    Integer.parseInt(tokens[i + 1]),    // hora de inicio
                    Integer.parseInt(tokens[i + 2]),    // hora de fim
                    Integer.parseInt(tokens[i + 3])));  // indice CM
        }
    }

    public static int maiorCMAcumulado(){
        if (d != 0) // há disciplinas
        {
            for(int i = 0; i < 7; i++){
                disciplinasSelecionadas[i] = maiorCMDia(disciplinasOfertadas[i]);
                cmAcumulado += calculaCMDia(disciplinasSelecionadas[i]);
            }
        }
        return cmAcumulado;
    }

    private static LinkedList<Disciplina> maiorCMDia(LinkedList<Disciplina> dia){
        LinkedList<Disciplina> temp = new LinkedList<Disciplina>();
        if(dia.size() >= 2)
        {
            for(int i = 0; i < dia.size()-1; i++)
            {
                if((dia.get(i).getC() >= dia.get(i+1).getF()) || (dia.get(i).getF() <= dia.get(i+1).getC()))
                {
                    for(int j = i+1; j < dia.size(); j++)
                        temp.add(dia.get(j));
                    maiorCMDia(temp);
                }
                else if (dia.get(i+1).getI() > dia.get(i).getI())
                    temp.add(dia.get(i+1));
                else
                    temp.add(dia.get(i));
            }
        }
        else
        {
            temp = dia;
        }
        return temp;
    }

    private static int calculaCMDia(LinkedList<Disciplina> dia){
        ListIterator<Disciplina> i = dia.listIterator();
        int cm = 0;
        while(i.hasNext())
            cm += i.next().getI();
        return cm;
    }

    public static void imprimirEntrada(){
        System.out.println(d);  // número de ofertas
        for(int i = 0; i < 7; i++)
        {
            ListIterator<Disciplina> dia = disciplinasOfertadas[i].listIterator();
            while(dia.hasNext()) {
                Disciplina temp = dia.next();
                // todas as linhas contendo as informações das disciplinasOfertadas
                System.out.println(temp.getS() + " " + temp.getC() + " " + temp.getF() + " " + temp.getI());
            }
        }
    }

    private static void imprimir(LinkedList<Disciplina> dis){
        for(int i = 0; i < dis.size(); i++)
            System.out.println(dis.get(i).getS() + " " + dis.get(i).getC() + " " + dis.get(i).getF() + " " + dis.get(i).getI());
    }

    public static void imprimirSelecionadas(){
        for(int i = 0; i < 7; i++){
            if (!disciplinasSelecionadas[i].isEmpty()){
                ListIterator<Disciplina> dia = disciplinasSelecionadas[i].listIterator();
                while(dia.hasNext()){
                    Disciplina temp = dia.next();
                    // todas as linhas contendo as informações das disciplinasOfertadas
                    System.out.println(temp.getS() + " " + temp.getC() + " " + temp.getF() + " " + temp.getI());
                }
            }
        }
    }

    public static void inicializarProblemaB(){
        cmAcumulado = 0;
        d = 0;
        for (int i = 0; i < 7; i++)
        {
            // Disciplinas Selecionadas
            if(disciplinasSelecionadas[i] == null)
                disciplinasSelecionadas[i] = new LinkedList<Disciplina>();
            else
                disciplinasSelecionadas[i].clear();
            // Disciplinas Ofertadas
            if(disciplinasOfertadas[i] == null)
                disciplinasOfertadas[i] = new LinkedList<Disciplina>();
            else
                disciplinasOfertadas[i].clear();;
        }
    }

    private static int indice(String dia){
        switch (dia){
            case "seg":
                return 0;
            case "ter":
                return 1;
            case "qua":
                return 2;
            case "qui":
                return 3;
            case "sex":
                return 4;
            case "sab":
                return 5;
            case "dom":
                return 6;
        }
        return -1;
    }
}

