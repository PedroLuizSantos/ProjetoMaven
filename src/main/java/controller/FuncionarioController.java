package controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.ConexaBD;
import model.Funcionarios;
import model.FuncionariosDao;

public class FuncionarioController implements Initializable {

	@FXML
	private Tab cadastrar;
	@FXML
	private Tab atualizar;
	@FXML
	private Tab consultar;
	@FXML
	private Tab sair;
	@FXML
	private Button buttonExit;

	@FXML
	private TextField nomeFuncionarioCadastro;
	@FXML
	private TextField cpfFuncionarioCadastro;
	@FXML
	private TextField cargoFuncionarioCadastro;
	@FXML
	private TextField salarioFuncionarioCadastro;

	@FXML
	private TextField nomeConsultaFuncionario;

	@FXML
	private TextField nomeAtualizarFuncionario;
	@FXML
	private TextField cpfAtualizarFuncionario;
	@FXML
	private TextField cargoAtualizarFuncionario;
	@FXML
	private TextField salarioAtualizarFuncionario;

	@FXML
	private TabPane abas;

	@FXML
	private TableView<Funcionarios> armazenarFuncionarios;

	@FXML
	private TableColumn<Funcionarios, Integer> idFuncionario;
	@FXML
	private TableColumn<Funcionarios, String> nomeFuncionario;
	@FXML
	private TableColumn<Funcionarios, String> cpfFuncionario;
	@FXML
	private TableColumn<Funcionarios, String> cargoFuncionario;
	@FXML
	private TableColumn<Funcionarios, Float> salarioFuncionario;

	public FuncionariosDao dao;
	private Funcionarios funcionarioSelecionado;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			Connection connection = new ConexaBD().getConnection();
			System.out.println("Sucesso Conexão com o Banco");
			connection.close();
		} catch (Exception e) {
			System.out.println("Falha na Conexão com o Banco");
			e.printStackTrace();
		}

		dao = new FuncionariosDao();

		idFuncionario.setCellValueFactory(new PropertyValueFactory<>("id"));
		nomeFuncionario.setCellValueFactory(new PropertyValueFactory<>("nome"));
		cpfFuncionario.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		cargoFuncionario.setCellValueFactory(new PropertyValueFactory<>("cargo"));
		salarioFuncionario.setCellValueFactory(new PropertyValueFactory<>("salario"));

	}

	@FXML
	void btnSair() {

		dialogBoxAviso("Fechando Programa...");
		Stage stage = (Stage) buttonExit.getScene().getWindow();
		stage.close();

	}

	@FXML
	void gerenciarAba() {

		if (cadastrar.isSelected() || consultar.isSelected()) {
			atualizar.setDisable(true);
			limparCadastroAtualizacao();
		}

	}

	@FXML
	private void limparFuncionarioDeletado() {
		armazenarFuncionarios.getItems().clear();
	}

	@FXML
	void limparDadosCadastro() {
		nomeFuncionarioCadastro.clear();
		cpfFuncionarioCadastro.clear();
		cargoFuncionarioCadastro.clear();
		salarioFuncionarioCadastro.clear();

	}

	@FXML
	private void limparCadastroAtualizacao() {
		nomeAtualizarFuncionario.clear();
		cpfAtualizarFuncionario.clear();
		cargoAtualizarFuncionario.clear();
		salarioAtualizarFuncionario.clear();
	}

	@FXML
	void efetuarDadosCadastro() {

		Funcionarios funcionario = new Funcionarios();
		funcionario.setNome(nomeFuncionarioCadastro.getText());
		funcionario.setCpf(cpfFuncionarioCadastro.getText());
		funcionario.setCargo(cargoFuncionarioCadastro.getText());
		funcionario.setSalario(new Float(salarioFuncionarioCadastro.getText()));

		try {
			dao.cadastrarFuncionario(funcionario);
			dialogBoxAviso("Cadastro Efetuado com Sucesso!");
			

		} catch (Exception e) {
			dialogBoxErro("Erro ao Efetuar o Cadastro :(");
			e.printStackTrace();
		}

	}

	@FXML
	void consultaFuncionario() {

		try {
			List<Funcionarios> resultado = dao.consultaFuncionario(nomeConsultaFuncionario.getText());

			if (resultado.isEmpty()) {

				dialogBoxAviso("Funcionário não Encontrado...");

			} else {
				armazenarFuncionarios.setItems(FXCollections.observableList(resultado));

			}
		} catch (Exception e) {
			dialogBoxErro("Erro ao Realizar Consulta");
			e.printStackTrace();
		}
	}

	@FXML
	void deletarFuncionario() {

		if (armazenarFuncionarios.getSelectionModel().getSelectedItem() == null) {
			dialogBoxErro("Por Favor selecione algum Funcionário.");

		} else {
			if (dialogBoxConfirmacao("Tem Certeza que deseja excluir o Funcionário ?")) {

				try {
					dao.deletarFuncionario(armazenarFuncionarios.getSelectionModel().getSelectedItem().getId());
					dialogBoxAviso("Funcionário foi Deletado com Êxito!");
					limparFuncionarioDeletado();

				} catch (Exception e) {
					dialogBoxErro("Erro ao Deletar Funcionário.");
					e.printStackTrace();
				}

			}

		}
	}

	@FXML
	void atualizarCadastroFuncionario() {

		funcionarioSelecionado.setNome(nomeAtualizarFuncionario.getText());
		funcionarioSelecionado.setCpf(cpfAtualizarFuncionario.getText());
		funcionarioSelecionado.setCargo(cargoAtualizarFuncionario.getText());
		funcionarioSelecionado.setSalario(new Float(salarioAtualizarFuncionario.getText()));

		try {
			dao.atualizarFuncionario(funcionarioSelecionado);
			dialogBoxAviso("Funcionário Atualizado com Sucesso!");
			abas.getSelectionModel().select(consultar);
			consultaFuncionario();

		} catch (Exception e) {
			dialogBoxErro("Falha na Atualização de Funcionário");
			e.printStackTrace();
		}
	}

	@FXML
	void atualizaFuncionario() {

		funcionarioSelecionado = armazenarFuncionarios.getSelectionModel().getSelectedItem();

		if (funcionarioSelecionado == null) {
			dialogBoxErro("Por Favor selecione algum Funcionário.");

		} else {
			atualizar.setDisable(false);
			nomeAtualizarFuncionario.setText(funcionarioSelecionado.getNome());
			cpfAtualizarFuncionario.setText(funcionarioSelecionado.getCpf());
			cargoAtualizarFuncionario.setText(funcionarioSelecionado.getCargo());
			salarioAtualizarFuncionario.setText(funcionarioSelecionado.getSalario().toString());
		}

	}

	public void dialogBoxAviso(String aviso) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Confirmação");
		alert.setHeaderText(null);
		alert.setContentText(aviso);

		alert.showAndWait();

	}

	public void dialogBoxErro(String erro) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erro Detectado");
		alert.setHeaderText(null);
		alert.setContentText(erro);

		alert.showAndWait();

	}

	private boolean dialogBoxConfirmacao(String confirmacao) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação de Comando");
		alert.setHeaderText(null);
		alert.setContentText(confirmacao);

		Optional<ButtonType> opcao = alert.showAndWait();

		if (opcao.get() == ButtonType.OK)
			return true;
		return false;

	}

}
