/* eslint-disable react/react-in-jsx-scope */
import { Navigate } from 'react-router-dom';
import Swal from 'sweetalert2';

function PrivateRoute({ children }: { children: React.ReactNode }) {
	const auth = localStorage.getItem('login-token');

	if (!auth)
		Swal.fire({
			title: '로그인 후 사용하실 수 있습니다.',
			confirmButtonText: '확인',
			showClass: {
				popup: 'animate__animated animate__fadeInDown',
			},
			hideClass: {
				popup: 'animate__animated animate__fadeOutUp',
			},
		});

	// eslint-disable-next-line react/react-in-jsx-scope
	return auth ? <>{children}</> : <Navigate to="/login"></Navigate>;
}

export default PrivateRoute;
