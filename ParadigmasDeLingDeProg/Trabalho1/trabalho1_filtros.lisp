;;-------------------------------------------------------------------------------
;; Trabalho 1 
;; Paradigmas de Linguagens de Programação
;;
;; Grupo: 
;; Fabio Gunkel  316520
;; Lucas David   407917
;; Pedro Vicente 407658 
;;        
;;-------------------------------------------------------------------------------

(defpackage "MY-PACKAGE"
	(:add-use-defaults t)
	(:use "COLOR" "CAPI")
)

(in-package "MY-PACKAGE")

;;----------------------------------------------------------------------------
;; Para compilar, execute "Compile and Load"
;; Para executar o programa, abra o listener e execute:
;; (my-package::rodar)
;;----------------------------------------------------------------------------

;; Função para layout da tela principal
(capi:define-interface programa ()
  ((imagem :initform nil))
  
  (:panes
   (viewer capi:output-pane
           :display-callback 'desenhaImagem
           :horizontal-scroll t
           :vertical-scroll t
           :visible-min-width 100
           :visible-min-height 100)
   (controller capi:push-button-panel
               :items '(:abrir :brilho :escurecer :mediana :media :bordas)
               :callbacks '(lerImagem
                            aplicarFiltro1
                            aplicarFiltro2
                            aplicarFiltro3
                            aplicarFiltro4
                            aplicarFiltro5)
               :callback-type :interface
               :print-function 'string-capitalize))
  (:layouts
   (main-layout capi:column-layout
                '(viewer controller)))
  (:default-initargs
   :layout 'main-layout
   :best-width 200
   :best-height 200))

;; Desenha uma imagem em um painel
(defun desenhaImagem (pane x y width height)
  (with-slots (imagem) (capi:top-level-interface pane)
    (when imagem
      (when (gp:rectangle-overlap x y (+ x width) (+ y height)
                                  0 0 (gp:image-width imagem) (gp:image-height imagem))
        (gp:draw-image pane imagem 0 0)))))

;; Definições dos tipos de arquivos permitidos
(defvar *image-file-filters* '("Bitmap" "*.bmp"))
(defvar *image-file-types* '("bmp"))

;; Redesenha a tela após aplicação de um filtro
(defun atualizaTela (interface)
  (with-slots (viewer imagem) interface
    (gp:invalidate-rectangle viewer)
    (capi:set-horizontal-scroll-parameters viewer :min-range 0 :max-range (if imagem (gp:image-width imagem) 0))
    (capi:set-vertical-scroll-parameters viewer :min-range 0 :max-range (if imagem (gp:image-height imagem) 0))))

;; Lê uma imagem e carrega para desenhá-la posteriormente
(defun lerImagem (interface)
  (with-slots (viewer imagem) interface
    (let ((file (capi:prompt-for-file "Escolha a imagem"
                                      :pathname (pathname-location #.(current-pathname))
                                      :filter (second *image-file-filters*)
                                      :filters *image-file-filters*
                                      :ok-check #'(lambda (file)
                                                    (member (pathname-type file) *image-file-types* :test 'equalp)))))
      (when (and file (probe-file file))
        (let ((external-image (gp:read-external-image file)))
          (when imagem
            (gp:free-image viewer imagem))
          (setf imagem (gp:load-image viewer external-image))
          (atualizaTela interface))))))

;; Transforma uma imagem em uma lista de listas de listas (matriz de cores)
;; Para implementação dos filtros
(defun carregarDadosImagem (interface acesso)
  (with-slots (viewer) interface
    (let ((dadosImagem '())
          (dadosLinha '()))
      (gp:image-access-transfer-from-image acesso)
      (dotimes (y (gp:image-access-height acesso))
        (setf dadosLinha '())
        (dotimes (x (gp:image-access-width acesso))
          (let ((pixel (gp:image-access-pixel acesso x y)))
            (setf dadosLinha (append dadosLinha (list (list (color-red (unconvert-color viewer pixel))
                                                            (color-green (unconvert-color viewer pixel))
                                                            (color-blue (unconvert-color viewer pixel))))))))
        (setf dadosImagem (append dadosImagem (list dadosLinha))))
      dadosImagem)))

;; Função auxiliar para manipulação das cores
(defun recuperaCor (dadosImagem x y)
  (let ((cor (nth x (nth y dadosImagem))))
    (make-rgb (nth 0 cor) (nth 1 cor) (nth 2 cor))))

;; Funções de tratamento dos eventos dos botões de filtros
(defun aplicarFiltro1 (interface)
  (aplicarFiltro interface 'filtro1))

(defun aplicarFiltro2 (interface)
  (aplicarFiltro interface 'filtro2))

(defun aplicarFiltro3 (interface)
  (aplicarFiltro interface 'filtro3))

(defun aplicarFiltro4 (interface)
  (aplicarFiltro interface 'filtro4))

(defun aplicarFiltro5 (interface)
  (aplicarFiltro interface 'filtro5))

;; Função que aplica um filtro e desenha a imagem resultante na tela
(defun aplicarFiltro (interface filtro)
  (with-slots (viewer imagem) interface
    (let* ((acesso (gp:make-image-access viewer imagem))
           (imagemFiltrada (funcall filtro (carregarDadosImagem interface acesso))))
    ;  (gp:image-access-transfer-from-image acesso)
      (dotimes (y (gp:image-access-height acesso))
        (dotimes (x (gp:image-access-width acesso))
          (setf (gp:image-access-pixel acesso x y) (convert-color viewer (recuperaCor imagemFiltrada x y)))))
      (gp:image-access-transfer-to-image acesso)
      (gp:free-image-access acesso)))
    (atualizaTela interface))

;; Filtro 1 - Brilho
(defun filtro1 (dadosImagem)
  (let ((imagemNova '()))
    (dotimes (x (length dadosImagem))
      (let ((linha (nth x dadosImagem))
            (linhaNova '()))
        (dotimes (y (length linha))
          (let* ((corAntiga (nth y linha))
                 (rAntigo (nth 0 corAntiga))
                 (gAntigo (nth 1 corAntiga))
                 (bAntigo (nth 2 corAntiga)))
            (setf linhaNova (append linhaNova (list (list (+ rAntigo 0.1) 
                                                          (+ gAntigo 0.1)
                                                          (+ bAntigo 0.1)))))))
        (setf imagemNova (append imagemNova (list linhaNova)))))
  imagemNova)
)

;; Filtro 2 - Escurecer
(defun filtro2 (dadosImagem)
  (let ((imagemNova '()))
    (dotimes (x (length dadosImagem))
      (let ((linha (nth x dadosImagem))
            (linhaNova '()))
        (dotimes (y (length linha))
          (let* ((corAntiga (nth y linha))
                 (rAntigo (nth 0 corAntiga))
                 (gAntigo (nth 1 corAntiga))
                 (bAntigo (nth 2 corAntiga)))
            (setf linhaNova (append linhaNova (list (list (- rAntigo 0.1) 
                                                          (- gAntigo 0.1)
                                                          (- bAntigo 0.1)))))))
        (setf imagemNova (append imagemNova (list linhaNova)))))
  imagemNova)
)

;; Filtro 3 - Mediana
(defun filtro3 (dadosImagem)
  (let ((imagemNova '()))
    (dotimes (x (length dadosImagem))
      (let ((linha (nth x dadosImagem))
            (linhaNova '()))
        (dotimes (y (length linha))
          (let* ((janela (pegaJanela dadosImagem x y))
                 (mediana (calculaMedianaLista janela)))
            (setf linhaNova (append linhaNova (list mediana)))))
        (setf imagemNova (append imagemNova (list linhaNova)))))
  imagemNova)
)

;; Filtro 4 - Média
(defun filtro4 (dadosImagem)
  (let ((imagemNova '()))
    (dotimes (x (length dadosImagem))
      (let ((linha (nth x dadosImagem))
            (linhaNova '()))
        (dotimes (y (length linha))
          (let* ((janela (pegaJanela dadosImagem x y))
                 (media (calculaMediaLista janela)))
            (setf linhaNova (append linhaNova (list media)))))
        (setf imagemNova (append imagemNova (list linhaNova)))))
  imagemNova)
)

;; Filtro 5 - Detecção de borda
(defun filtro5 (dadosImagem)
    (let ((imagemNova '()))
    (dotimes (x (length dadosImagem))
      (let ((linha (nth x dadosImagem))
            (linhaNova '()))
        (dotimes (y (length linha))
          (let* ((janela (pegaJanela dadosImagem x y))
                 (borda  (calculaMascara janela)))
            (setf linhaNova (append linhaNova (list borda)))))
        (setf imagemNova (append imagemNova (list linhaNova)))))
  imagemNova)
)

;; Função auxiliar que retorna uma janela, dadas uma lista com a imagem
;; e as coordenadas x e y
(defun pegaJanela (dadosImagem x y)
  (list (pegaPixel dadosImagem (- x 1) (- y 1)) 
        (pegaPixel dadosImagem (- x 1) y)
        (pegaPixel dadosImagem (- x 1) (+ y 1))
        (pegaPixel dadosImagem x (- y 1))
        (pegaPixel dadosImagem x y)
        (pegaPixel dadosImagem x (+ y 1))
        (pegaPixel dadosImagem (+ x 1) (- y 1))
        (pegaPixel dadosImagem (+ x 1) y)
        (pegaPixel dadosImagem (+ x 1) (+ y 1))))

;; Função auxiliar para a janela
;; Apenas retorna um pixel, mas com um cuidado
;; especial para os cantos da imagem
(defun pegaPixel (dadosImagem x y)
  (if (< x 0) (setq x 0))
  (if (< y 0) (setq y 0))
  (if (>= x (length dadosImagem)) (setq x (- (length dadosImagem) 2)))
  (let ((linha (nth x dadosImagem)))
    (if (>= y (length linha)) (setq y (- (length linha) 2)))
    (nth y linha)))

;; Calcula a média de uma lista de pixels
;; Observe como a função enésimos separa as cores R, G e B
(defun calculaMediaLista (lista)
  (let ((listaR (enesimos 0 lista))
        (listaG (enesimos 1 lista))
        (listaB (enesimos 2 lista)))
    (list (media listaR) (media listaG) (media listaB))))

;;Calcula a mediana de uma lista de pixels
;;Tendo a função enésimos o objetivo de separar as cores R, G e B
(defun calculaMedianaLista (lista)
  (let ((listaR (enesimos 0 lista))
        (listaG (enesimos 1 lista))
        (listaB (enesimos 2 lista)))
    (list (mediana listaR) (mediana listaG) (mediana listaB))))


;;Define qua processo será usado para calcular a mediana de acordo
;;com o número de elementos da lista
(defun mediana (lista) (cond ((oddp (length lista)) (medianaImpar lista))
                              ( t (medianaPar lista)))
)

;;Calcula a mediana de uma lista com número ímpar de elementos
(defun medianaImpar (lista)
   
  (nth (floor (length lista) 2) (sort lista '<))
)

;;Calcula a mediana de uma lista de número par de elementos
(defun medianaPar (lista)
  (/ (+ (nth (floor (length lista) 2)  (sort lista '<))  (nth (- (floor (length lista) 2) 1) (sort lista '<) )) 2))


;;Calcula o valor da máscara  de uma lista de pixels
;;Tendo a função enésimos o objetivo de separar as cores R, G e B
(defun calculaMascara (lista)
 (let ((listaR (enesimos 0 lista))
        (listaG (enesimos 1 lista))
        (listaB (enesimos 2 lista)))
     (list (- 1 (borda listaR)) (- 1 (borda listaG)) (- 1 (borda listaB) ) )
 )
)

;;Calcula o valor da obtido aplicando a máscara definida a uma janela de pixels
(defun borda (lista)
(let ((soma 0))
    (setf soma (+ (nth 1 lista) (nth 3 lista)  (* (nth 4 lista) -4) (*(nth 8 lista) )) )
    soma))

;(defun m4 (a)
;    (let (
;         (r (nth 0 a))
;         (g (nth 1 a))
;         (b (nth 2 a))
;        )
;        (list (* r  -4) (* g  -4) (* b  -4))
;     ) 
; 
;)
;
;(defun separa(lista)
;   (let ((listaR(enesimos 0 lista))
;        (listaG (enesimos 1 lista))
;        (listaB (enesimos 2 lista)))
;    (list (apply '+ listaR) (apply '+ listaG)(apply '+ listaB) ))
;)



;; Calcula a média de uma lista numérica
(defun media (lista)
  (let ((soma 0))
    (dolist (var lista)
      (setf soma (+ soma var)))
    (/ soma (length lista))))

;; Retorna os enésimos elementos de uma lista, começando a partir de 0
;; Ex: (enesimos 1 '((0 1 2)(3 4 5)(6 7 8))) -> (1 4 7)
(defun enesimos (n lista)
  (let ((retorno '()))
    (dolist (var lista)
      (setf retorno (append retorno (list (nth n var)))))
    retorno))

;; Programa principal
(defun rodar ()
  (capi:display (make-instance 'programa)))
