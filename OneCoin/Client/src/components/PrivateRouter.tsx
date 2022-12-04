/* eslint-disable react/react-in-jsx-scope */
import { Navigate } from 'react-router-dom';
import Swal from 'sweetalert2';

import { useRecoilValue } from 'recoil';
import { isLogin } from '../store';

function PrivateRoute({ children }: { children: React.ReactNode }) {
	const login = useRecoilValue(isLogin);
	const Authorization = sessionStorage.getItem('login-token');

	if (!login && !Authorization)
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
	return login || Authorization ? (
		<>{children}</>
	) : (
		<Navigate to="/login"></Navigate>
	);
}

export default PrivateRoute;
