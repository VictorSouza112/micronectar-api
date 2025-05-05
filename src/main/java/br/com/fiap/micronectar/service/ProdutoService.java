package br.com.fiap.micronectar.service;

import br.com.fiap.micronectar.dto.ProdutoCadastroDTO;
import br.com.fiap.micronectar.dto.ProdutoExibicaoDTO;
import br.com.fiap.micronectar.enums.UsuarioRole;
import br.com.fiap.micronectar.exception.AcessoNegadoException;
import br.com.fiap.micronectar.exception.MicroempreendedorNotFoundException;
import br.com.fiap.micronectar.model.Microempreendedor;
import br.com.fiap.micronectar.model.Produto;
import br.com.fiap.micronectar.model.Usuario; // Importar Usuario
import br.com.fiap.micronectar.repository.MicroempreendedorRepository;
import br.com.fiap.micronectar.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils; // Para copiar propriedades DTO -> Entidade
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; // Importar UserDetails
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MicroempreendedorRepository microempreendedorRepository;

    @Transactional
    public ProdutoExibicaoDTO adicionarProduto(Long microempreendedorId, ProdutoCadastroDTO dto, UserDetails usuarioLogado) {

        // 1. Buscar o Microempreendedor pelo ID fornecido na URL
        Microempreendedor microempreendedor = microempreendedorRepository.findById(microempreendedorId)
                .orElseThrow(() -> new MicroempreendedorNotFoundException("Microempreendedor não encontrado com ID: " + microempreendedorId));

        // 2. Verificar Autorização
        // Faz o cast do UserDetails para a nossa entidade Usuario
        if (!(usuarioLogado instanceof Usuario)) {
            // Isso não deveria acontecer se o UserDetailsService estiver correto, mas é uma segurança
            throw new AcessoNegadoException("Tipo de usuário logado inválido para esta operação.");
        }
        Usuario user = (Usuario) usuarioLogado;

        // Verifica se é ADMIN OU (se é ME E é o dono do perfil)
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(UsuarioRole.ROLE_ADMIN.getRole()));
        boolean isOwner = user.getRole() == UsuarioRole.ROLE_MICROEMPREENDEDOR && user.getIdUsuario().equals(microempreendedorId);

        if (!isAdmin && !isOwner) {
            throw new AcessoNegadoException("Usuário não autorizado a adicionar produtos para este microempreendedor.");
        }

        // 3. Criar e mapear a entidade Produto
        Produto novoProduto = new Produto();
        // Copia propriedades com nomes correspondentes do DTO para a entidade
        BeanUtils.copyProperties(dto, novoProduto);

        // 4. Associar o Microempreendedor ao Produto
        novoProduto.setMicroempreendedor(microempreendedor);

        // 5. Salvar o Produto no banco
        Produto produtoSalvo = produtoRepository.save(novoProduto);

        // 6. Retornar o DTO de exibição
        return new ProdutoExibicaoDTO(produtoSalvo);
    }

    // Listar Produtos por Microempreendedor
    public List<ProdutoExibicaoDTO> listarProdutosPorMicroempreendedor(Long microempreendedorId) {

        // 1. Verificar se o Microempreendedor existe
        if (!microempreendedorRepository.existsById(microempreendedorId)) {
            throw new MicroempreendedorNotFoundException("Microempreendedor não encontrado com ID: " + microempreendedorId);
        }

        // 2. Buscar os produtos associados usando o método do repositório
        List<Produto> produtos = produtoRepository.findByMicroempreendedorIdUsuario(microempreendedorId);

        // 3. Converter a lista de entidades Produto para uma lista de ProdutoExibicaoDTO
        return produtos.stream()                 // Cria um stream da lista de produtos
                .map(ProdutoExibicaoDTO::new) // Para cada produto, cria um novo DTO usando o construtor
                .toList();                     // Coleta os DTOs em uma nova lista (Java 16+)
    }

}