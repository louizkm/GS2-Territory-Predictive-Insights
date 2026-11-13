package br.edu.fiap.territorio.excecao;

public class FonteIndisponivelException extends RuntimeException {
    public FonteIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
