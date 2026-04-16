export default function StatCard({ label, value, icon: Icon }) {
  return (
    <div className="bg-gray-800 rounded p-4 flex items-center gap-4">
      {Icon && <Icon className="w-8 h-8 text-blue-500" />}
      <div>
        <div className="text-gray-400 text-sm">{label}</div>
        <div className="text-2xl font-bold">{value}</div>
      </div>
    </div>
  );
}
