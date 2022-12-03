import Swal from 'sweetalert2';

function Alert(title: string) {
	return Swal.fire({
		title: title,
		confirmButtonText: '확인',
		showClass: {
			popup: 'animate__animated animate__fadeInDown',
		},
		hideClass: {
			popup: 'animate__animated animate__fadeOutUp',
		},
	});
}

export default Alert;
