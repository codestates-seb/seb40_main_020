/* eslint-disable react/react-in-jsx-scope */
import { Navigate } from 'react-router-dom';

function PrivateRoute({ children }: { children: React.ReactNode }) {
	const auth = localStorage.getItem('login-token');

	if (!auth) alert('로그인 후 사용하실 수 있습니다.');
	// eslint-disable-next-line react/react-in-jsx-scope
	return auth ? <>{children}</> : <Navigate to="/login"></Navigate>;
}

export default PrivateRoute;
